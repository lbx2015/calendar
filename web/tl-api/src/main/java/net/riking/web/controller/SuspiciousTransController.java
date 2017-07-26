package net.riking.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.config.Config;
import net.riking.core.entity.JobEvent;
import net.riking.core.entity.Resp;
import net.riking.core.entity.TokenHolder;
import net.riking.core.log.InModLog;
import net.riking.core.workflow.EventResult;
import net.riking.core.workflow.WorkflowMgr;
import net.riking.entity.PageQuery;
import net.riking.entity.model.AmlSuspicious;
import net.riking.entity.model.AmlSuspiciousQuery;
import net.riking.service.UserBranchServiceImpl;
import net.riking.service.repo.AmlRuleEngineRepo;
import net.riking.service.repo.AmlSuspiciousRepo;
import net.riking.service.repo.CrimeTypeRepo;

@InModLog(modName = "可疑分析处理")
@RestController
@RequestMapping("/suspiciousTrans")
public class SuspiciousTransController {
	@Autowired
	Config config;

	@Autowired
	AmlSuspiciousRepo suspiciousRepo;

	@Autowired
	AmlRuleEngineRepo amlRuleEngineRepo;
	@Autowired
	WorkflowMgr workflowMgr;

	@Autowired
	ObjectMapper mapper = new ObjectMapper();

	@Autowired
	UserBranchServiceImpl userBranchServiceImpl;

	@Autowired
	CrimeTypeRepo crimeTypeRepo;

	/*@ApiOperation(value = "重新启用", notes = "POST-@AmlSuspicious")
	@RequestMapping(value = "/resave", method = RequestMethod.GET)
	public Resp resave(@RequestParam("id") Long id) {
		String userName = TokenHolder.get().getUserName();
		AmlSuspicious aml = suspiciousRepo.findOne(id);
		JobEvent jobEvent = new JobEvent(aml.getJob().getJobId(), "RESAVE", userName);
		workflowMgr.sendEvents(Arrays.asList(jobEvent));
		return new Resp(aml, CodeDef.SUCCESS);
	}*/

	@ApiOperation(value = "批量更新可疑交易信息", notes = "POST-@AmlSuspicious")
	@RequestMapping(value = "/updateMore", method = RequestMethod.POST)
	public Resp updateMore(@RequestBody AmlSuspicious aml) {
		String userName = TokenHolder.get().getUserName();
		List<AmlSuspicious> list = suspiciousRepo.findAll(new Specification<AmlSuspicious>() {
			@Override
			public Predicate toPredicate(Root<AmlSuspicious> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				if (StringUtils.isNotEmpty(aml.getTosc())) {
					list.add(cb.like((root.get("tosc").as(String.class)), "%" + aml.getTosc() + "%"));
				}
				if (StringUtils.isNotEmpty(aml.getCtid())) {
					list.add(cb.equal((root.get("ctid").as(String.class)), aml.getCtid()));
				}
				if (StringUtils.isNotEmpty(aml.getTicd())) {
					list.add(cb.like((root.get("ticd").as(String.class)), "%" + aml.getTicd() + "%"));
				}
				list.add(cb.equal((root.get("deleteState").as(String.class)), "1"));
				// 非超级管理员
				if (null != TokenHolder.get().getBranchId()) {
					List<String> branchCodes = userBranchServiceImpl.get(TokenHolder.get().getUserId());
					if (branchCodes != null && branchCodes.size() > 0) {
						list.add(root.get("jgbm").as(String.class).in(branchCodes));
					}
				}
				if (aml.getStarts() != null || aml.getEnds() != null) {
					if (aml.getEnds() == null) {
						list.add(cb.greaterThanOrEqualTo(root.<Date> get("rpdt"), aml.getStarts()));
					} else if (aml.getStarts() == null) {
						list.add(cb.lessThanOrEqualTo(root.<Date> get("rpdt"), aml.getEnds()));
					} else {
						list.add(cb.between(root.<Date> get("rpdt"), aml.getStarts(), aml.getEnds()));
					}
				}
				if (aml.getJob() != null) {
					if (StringUtils.isNotEmpty(aml.getJob().getCurJobState())) {
//						Join<AmlSuspicious, Job> depJoin = root
//								.join(root.getModel().getSingularAttribute("job", Job.class), JoinType.LEFT);
						list.add(cb.like(root.get("flowState").as(String.class),
								"%" + aml.getJob().getCurJobState() + "%"));
					}
				}
				Predicate[] p = new Predicate[list.size()];
				query.where(cb.and(list.toArray(p)));
				return query.getRestriction();
			}
		});
//		Set<Long> set = new HashSet<Long>();
//		for (AmlSuspicious amls : list) {
//			set.add(amls.getId());
//		}
		//int i = suspiciousRepo.updateMore(aml.getTkms(), aml.getDorp(), aml.getSsds(), set);
		String event = null;
		ArrayList<JobEvent> list2 = new ArrayList<JobEvent>();
		event = "SISP_DEAL";
		//workflowMgr.fillJobsInfo(list);
		for (AmlSuspicious amls : list) {
			JobEvent jobEvent = new JobEvent(amls.getJobId(), event, userName);
			amls.setDetr(aml.getDetr());
			amls.setDorp(aml.getDorp());
			amls.setOdrp(aml.getOdrp());
			amls.setTptr(aml.getTptr());
			amls.setOtpr(aml.getOtpr());
			amls.setStcb(aml.getStcb());
			amls.setAosp(aml.getAosp());
			amls.setAptp("01");
			jobEvent.setFireEvent(false);
			list2.add(jobEvent);
		}
		int count = 0;
		List<EventResult> results = workflowMgr.sendEvents(list2);
		for (EventResult eventResult : results) {
			if (eventResult.isSuccess()) {
				count++;
			}
		}
		return new Resp(results.size() - count, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "批量审核可疑交易信息", notes = "POST-@AmlSuspicious")
	@RequestMapping(value = "/approveMore", method = RequestMethod.POST)
	public Resp approveMore(@RequestBody AmlSuspicious aml) {
		String userName = TokenHolder.get().getUserName();
		List<AmlSuspicious> list = suspiciousRepo.findAll(new Specification<AmlSuspicious>() {
			@Override
			public Predicate toPredicate(Root<AmlSuspicious> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				if (StringUtils.isNotEmpty(aml.getTosc())) {
					list.add(cb.like((root.get("tosc").as(String.class)), "%" + aml.getTosc() + "%"));
				}
				if (StringUtils.isNotEmpty(aml.getCtid())) {
					list.add(cb.equal((root.get("ctid").as(String.class)), aml.getCtid()));
				}
				if (StringUtils.isNotEmpty(aml.getTicd())) {
					list.add(cb.like((root.get("ticd").as(String.class)), "%" + aml.getTicd() + "%"));
				}
				list.add(cb.equal((root.get("deleteState").as(String.class)), "1"));
				// 非超级管理员
				if (null != TokenHolder.get().getBranchId()) {
					List<String> branchCodes = userBranchServiceImpl.get(TokenHolder.get().getUserId());
					if (branchCodes != null && branchCodes.size() > 0) {
						list.add(root.get("jgbm").as(String.class).in(branchCodes));
					}
				}
				if (aml.getStarts() != null || aml.getEnds() != null) {
					if (aml.getEnds() == null) {
						list.add(cb.greaterThanOrEqualTo(root.<Date> get("rpdt"), aml.getStarts()));
					} else if (aml.getStarts() == null) {
						list.add(cb.lessThanOrEqualTo(root.<Date> get("rpdt"), aml.getEnds()));
					} else {
						list.add(cb.between(root.<Date> get("rpdt"), aml.getStarts(), aml.getEnds()));
					}
				}
				if (aml.getJob() != null) {
					if (StringUtils.isNotEmpty(aml.getJob().getCurJobState())) {
						//Join<AmlSuspicious, Job> depJoin = root.join(root.getModel().getSingularAttribute("job", Job.class), JoinType.LEFT);
						list.add(cb.like(root.get("flowState").as(String.class),
								"%" + aml.getJob().getCurJobState() + "%"));
					}
				}
				Predicate[] p = new Predicate[list.size()];
				query.where(cb.and(list.toArray(p)));
				return query.getRestriction();
			}
		});
//		Set<Long> set = new HashSet<Long>();
//		for (AmlSuspicious amls : list) {
//			set.add(amls.getId());
//		}
//		aml.setAptp("01");
//		int i = suspiciousRepo.approveMore(aml.getTkms(), aml.getDorp(), aml.getSsds(), aml.getAptp(), set);
		String event = null;
		ArrayList<JobEvent> list2 = new ArrayList<JobEvent>();
		event = "SISP_VERIFY_PASS";
//		workflowMgr.fillJobsInfo(list);
		for (AmlSuspicious amls : list) {
			JobEvent jobEvent = new JobEvent(amls.getJobId(), event, userName);
			amls.setDetr(aml.getDetr());
			amls.setDorp(aml.getDorp());
			amls.setOdrp(aml.getOdrp());
			amls.setTptr(aml.getTptr());
			amls.setOtpr(aml.getOtpr());
			amls.setStcb(aml.getStcb());
			amls.setAosp(aml.getAosp());
			amls.setAptp("01");
//			jobEvent.setTargetJson(mapper.writeValueAsString(amls));
			jobEvent.setFireEvent(false);
			list2.add(jobEvent);
		}
		int count = 0;
		List<EventResult> results = workflowMgr.sendEvents(list2);
		for (EventResult eventResult : results) {
			if (eventResult.isSuccess()) {
				count++;
			}
		}
		return new Resp(results.size() - count, CodeDef.SUCCESS);
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public Resp get_(@RequestParam("id") Long id) {
		AmlSuspicious suspicious = suspiciousRepo.findOne(id);
		workflowMgr.fillJobsInfo(Arrays.asList(suspicious));
		String tstp = suspicious.getTstp();
		if (StringUtils.isNotEmpty(tstp) && tstp.length() >= 6) {
			if (tstp.substring(0, 2).equals("00")) {
				suspicious.setTstp(tstp.substring(0, 2) + "," + tstp.substring(2, 4) + "," + tstp.substring(4));
			} else {
				suspicious.setTstp(tstp.substring(0, 2) + "," + tstp.substring(2));
			}
		}
		String cfrc = suspicious.getCfrc();
		if (StringUtils.isNotEmpty(cfrc) && cfrc.length() > 3) {
			String cfrcNew = cfrc.substring(0, 3) + "," + cfrc.substring(3);
			suspicious.setCfrc(cfrcNew);
		}
		String trcd = suspicious.getTrcd();
		if (StringUtils.isNotEmpty(trcd) && trcd.length() > 3) {
			String trcdNew = trcd.substring(0, 3) + "," + trcd.substring(3);
			suspicious.setTrcd(trcdNew);
		}
		return new Resp(suspicious, CodeDef.SUCCESS);
	}

	@RequestMapping(value = "/getMore", method = RequestMethod.GET)
	public Resp getMore(@ModelAttribute PageQuery query, @ModelAttribute AmlSuspicious aml) {
		PageRequest pageable = new PageRequest(query.getPindex(), query.getPcount(), query.getSortObj());
		Page<AmlSuspicious> page = suspiciousRepo.findAll(new Specification<AmlSuspicious>() {
			@Override
			public Predicate toPredicate(Root<AmlSuspicious> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				if (StringUtils.isNotEmpty(aml.getTosc())) {
					list.add(cb.like((root.get("tosc").as(String.class)), "%" + aml.getTosc() + "%"));
				}
				if (StringUtils.isNotEmpty(aml.getCtid())) {
					list.add(cb.equal((root.get("ctid").as(String.class)), aml.getCtid()));
				}
				if (StringUtils.isNotEmpty(aml.getTicd())) {
					list.add(cb.like((root.get("ticd").as(String.class)), "%" + aml.getTicd() + "%"));
				}
				list.add(cb.equal((root.get("deleteState").as(String.class)), "1"));
				// 非超级管理员
				if (null != TokenHolder.get().getBranchId()) {
					List<String> branchCodes = userBranchServiceImpl.get(TokenHolder.get().getUserId());
					if (branchCodes != null && branchCodes.size() > 0) {
						list.add(root.get("jgbm").as(String.class).in(branchCodes));
					}
				}
				if (aml.getStarts() != null || aml.getEnds() != null) {
					if (aml.getEnds() == null) {
						list.add(cb.greaterThanOrEqualTo(root.<Date> get("rpdt"), aml.getStarts()));
					} else if (aml.getStarts() == null) {
						list.add(cb.lessThanOrEqualTo(root.<Date> get("rpdt"), aml.getEnds()));
					} else {
						list.add(cb.between(root.<Date> get("rpdt"), aml.getStarts(), aml.getEnds()));
					}
				}
				if (aml.getJob() != null) {
					if (StringUtils.isNotEmpty(aml.getJob().getCurJobState())) {
						//Join<AmlSuspicious, Job> depJoin = root.join(root.getModel().getSingularAttribute("job", Job.class), JoinType.LEFT);
						list.add(cb.like(root.get("flowState").as(String.class),aml.getJob().getCurJobState()));
					}
				} else {
//					Join<AmlSuspicious, Job> depJoin = root.join(root.getModel().getSingularAttribute("job", Job.class),
//							JoinType.LEFT);
					list.add(cb.like(root.get("flowState").as(String.class), "%SISP_PRE_%"));
				}
				Predicate[] p = new Predicate[list.size()];
				query.where(cb.and(list.toArray(p)));
				query.orderBy(cb.desc(root.get("id").as(Long.class)));  
				return query.getRestriction();
			}
		}, pageable);
		for (AmlSuspicious suspicious : page.getContent()) {
			String tstp = suspicious.getTstp();
			if (StringUtils.isNotEmpty(tstp) && tstp.length() >= 6) {
				if (tstp.substring(0, 2).equals("00")) {
					suspicious.setTstp(tstp.substring(0, 2) + "," + tstp.substring(2, 4) + "," + tstp.substring(4));
				} else {
					suspicious.setTstp(tstp.substring(0, 2) + "," + tstp.substring(2));
				}
			}
		}
		workflowMgr.fillJobsInfo(page.getContent());
		return new Resp(page);
	}

	@RequestMapping(value = "/getNames", method = RequestMethod.GET)
	public Resp getNames() {
		Set<String> status = new HashSet<String>();
		status.add("SISP_PRE_VERIFY");
		status.add("SISP_PRE_DEAL");
		List<AmlSuspiciousQuery> list = null;
		// 非超级管理员
		if (null != TokenHolder.get().getBranchId()) {
			List<String> branchCodes = userBranchServiceImpl.get(TokenHolder.get().getUserId());
			list = suspiciousRepo.findAllCtid(status, branchCodes);
		} else {
			list = suspiciousRepo.findAllCtid(status);
		}
		Iterator<AmlSuspiciousQuery> iterator = list.iterator();
		while (iterator.hasNext()) {
			AmlSuspiciousQuery query = iterator.next();
			if (StringUtils.isEmpty(query.getCtid())) {
				iterator.remove();
			}
		}
		return new Resp(list);
	}

}

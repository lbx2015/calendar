package net.riking.web.controller;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.config.Config;
import net.riking.core.entity.JobEvent;
import net.riking.core.entity.Resp;
import net.riking.core.entity.TokenHolder;
import net.riking.core.entity.model.Job;
import net.riking.core.log.InFunLog;
import net.riking.core.log.InModLog;
import net.riking.core.workflow.EventResult;
import net.riking.core.workflow.WorkflowMgr;
import net.riking.entity.BigAmountMz;
import net.riking.entity.PageQuery;
import net.riking.entity.model.BigAmount;
import net.riking.entity.model.Branch;
import net.riking.entity.model.CheckResult;
import net.riking.service.BigCheckServiceImpl;
import net.riking.service.BigSuspCheckServiceImpl;
import net.riking.service.UserBranchServiceImpl;
import net.riking.service.repo.BigAmountRepo;

@InModLog(modName = "大额交易报送")
@Repository
@RestController
@RequestMapping(value = "/bigamount")
public class BigAmountController {
	@Autowired
	Config config;

	@Autowired
	WorkflowMgr workflowMgr;

	@Autowired
	ObjectMapper mapper = new ObjectMapper();

	@Autowired
	BigAmountRepo bigAmountRepo;

	@Autowired
	BigCheckServiceImpl bigCheckService;

	@Autowired
	BigSuspCheckServiceImpl bigSuspCheckService;

	@Autowired
	private UserBranchServiceImpl userBranchServiceImpl;

	// @InitBinder
	// protected void initBinder(WebDataBinder binder) {
	// binder.registerCustomEditor(Date.class, new CustomDateEditor(new
	// SimpleDateFormat("yyyy-MM-dd"), true));
	// }

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public Resp get_(@RequestParam("id") Long id) {
		BigAmount bigAmount = bigAmountRepo.findOne(id);
		// String uuId = bigAmount.getJob().getJobId();
		String trcd = bigAmount.getTrcd();
		if (StringUtils.isNotEmpty(trcd) && trcd.length() > 3) {
			String trcdNew = trcd.substring(0, 3) + "," + trcd.substring(3);
			bigAmount.setTrcd(trcdNew);
		}
		String tstp = bigAmount.getTstp();
		if (StringUtils.isNotEmpty(tstp) && tstp.length() >= 6) {
			if (tstp.substring(0, 2).equals("00")) {
				bigAmount.setTstp(tstp.substring(0, 2) + "," + tstp.substring(2, 4) + "," + tstp.substring(4));
			} else {
				bigAmount.setTstp(tstp.substring(0, 2) + "," + tstp.substring(2));
			}
		}
		String tdrc = bigAmount.getCfrc();
		if (StringUtils.isNotEmpty(tdrc) && tdrc.length() >= 3) {
			bigAmount.setCfrc(tdrc.substring(0, 3) + "," + tdrc.substring(3));
		}
		workflowMgr.fillJobsInfo(Arrays.asList(bigAmount));
		return new Resp(bigAmount, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "复制")
	@RequestMapping(value = "/getes", method = RequestMethod.GET)
	public Resp getes_(@RequestParam("id") Long id) {
		BigAmount bigAmount = bigAmountRepo.findOne(id);
		bigAmount.setTicd(null);
		bigAmount.setJob(null);
		bigAmount.setId(null);
		bigAmount.setTname(null);
		bigAmount.setSubmitBatch(null);
		bigAmount.setNowSubmitBatch(null);
		bigAmount.setJobId(null);
		bigAmount.setFlowState(null);
		bigAmount.setFlowOwner(null);
		String trcd = bigAmount.getTrcd();
		if (StringUtils.isNotEmpty(trcd) && trcd.length() > 3) {
			String trcdNew = trcd.substring(0, 3) + "," + trcd.substring(3);
			bigAmount.setTrcd(trcdNew);
		}
		String tstp = bigAmount.getTstp();
		if (StringUtils.isNotEmpty(tstp) && tstp.length() >= 6) {
			if (tstp.substring(0, 2).equals("00")) {
				bigAmount.setTstp(tstp.substring(0, 2) + "," + tstp.substring(2, 4) + "," + tstp.substring(4));
			} else {
				bigAmount.setTstp(tstp.substring(0, 2) + "," + tstp.substring(2));
			}
		}
		String tdrc = bigAmount.getCfrc();
		if (StringUtils.isNotEmpty(tdrc) && tdrc.length() >= 3) {
			bigAmount.setCfrc(tdrc.substring(0, 3) + "," + tdrc.substring(3));
		}
		return new Resp(bigAmount, CodeDef.SUCCESS);
	}

	@RequestMapping(value = "/getMore", method = RequestMethod.GET)
	public Resp getMore(@ModelAttribute PageQuery query, @ModelAttribute BigAmount bigAmount) throws Exception {
		PageRequest pageable = new PageRequest(query.getPindex(), query.getPcount(), query.getSortObj());
		Specification<BigAmount> b1 = null;
		if (bigAmount.getJob() != null && bigAmount.getJob().getCurJobState() != null) {
			b1 = new Specification<BigAmount>() {
				@Override
				public Predicate toPredicate(Root<BigAmount> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					List<Predicate> list = new ArrayList<Predicate>();
//					Join<BigAmount, Job> depJoin = root.join(root.getModel().getSingularAttribute("job", Job.class),
//							JoinType.LEFT);
					list.add(cb.equal((root.get("flowState").as(String.class)),
							bigAmount.getJob().getCurJobState()));

					Predicate[] p = new Predicate[list.size()];
					return cb.or(list.toArray(p));
				}
			};
		} else {
			b1 = new Specification<BigAmount>() {

				@Override
				public Predicate toPredicate(Root<BigAmount> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					List<Predicate> list = new ArrayList<Predicate>();
//					Join<BigAmount, Job> depJoin = root.join(root.getModel().getSingularAttribute("job", Job.class),
//							JoinType.LEFT);
					if (bigAmount.getUpState() != null && bigAmount.getUpState().equals("NoUp")) {
						list.add(cb.equal((root.get("flowState").as(String.class)), "PRE_RECROD"));
						list.add(cb.equal((root.get("flowState").as(String.class)), "PRE_SUBMIT"));
						list.add(cb.equal((root.get("flowState").as(String.class)), "PRE_VERIFY"));
						list.add(cb.equal((root.get("flowState").as(String.class)), "PRE_EXPORT"));
					} else if (bigAmount.getUpState() != null && bigAmount.getUpState().equals("Up")) {
						list.add(cb.equal((root.get("flowState").as(String.class)), "PRE_EXPORTOVER"));
						list.add(cb.equal((root.get("flowState").as(String.class)), "PRE_STORAGE"));
					} else {
						list.add(cb.equal((root.get("flowState").as(String.class)), "PRE_RECROD"));
						list.add(cb.equal((root.get("flowState").as(String.class)), "PRE_SUBMIT"));
						list.add(cb.equal((root.get("flowState").as(String.class)), "PRE_VERIFY"));
						list.add(cb.equal((root.get("flowState").as(String.class)), "PRE_EXPORT"));
						list.add(cb.equal((root.get("flowState").as(String.class)), "PRE_EXPORTOVER"));
						list.add(cb.equal((root.get("flowState").as(String.class)), "PRE_STORAGE"));
					}
					Predicate[] p = new Predicate[list.size()];
					return cb.or(list.toArray(p));
				}
			};
		}

		Specification<BigAmount> b2 = new Specification<BigAmount>() {
			@Override
			public Predicate toPredicate(Root<BigAmount> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				if (null != bigAmount.getCsnm()) {
					list.add(cb.equal((root.get("csnm").as(String.class)),  bigAmount.getCsnm()));
				}
				if (null != bigAmount.getCrcd()) {
					list.add(cb.equal((root.get("crcd").as(String.class)), bigAmount.getCrcd()));
				}
				if (null != bigAmount.getTsdr()) {
					list.add(cb.equal((root.get("tsdr").as(Integer.class)), bigAmount.getTsdr()));
				}
				if (bigAmount.getCtnm() != null) {
					list.add(cb.like((root.get("ctnm").as(String.class)), "%" + bigAmount.getCtnm() + "%"));
				}
				if (bigAmount.getTicd() != null) {
					list.add(cb.like((root.get("ticd").as(String.class)), "%" + bigAmount.getTicd() + "%"));
				}
				if (bigAmount.getStarts() != null || bigAmount.getEnds() != null) {
					if (bigAmount.getEnds() == null) {
						list.add(cb.greaterThanOrEqualTo(root.<Date> get("rpdt"), bigAmount.getStarts()));
					} else if (bigAmount.getStarts() == null) {
						list.add(cb.lessThanOrEqualTo(root.<Date> get("rpdt"), bigAmount.getEnds()));
					} else {
						list.add(cb.between(root.<Date> get("rpdt"), bigAmount.getStarts(), bigAmount.getEnds()));
					}
				}
				list.add(cb.equal((root.get("deleteState").as(String.class)), "1"));
				// 非超级管理员
				if (null != TokenHolder.get().getBranchId()) {
					List<String> branchCodes = userBranchServiceImpl.get(TokenHolder.get().getUserId());
					if (branchCodes != null && branchCodes.size() > 0) {
						list.add(root.get("jgbm").as(String.class).in(branchCodes));
					}
				}
				Predicate[] p = new Predicate[list.size()];
				query.where(cb.and(list.toArray(p)));
				query.orderBy(cb.desc(root.get("id").as(Long.class)));  
				return query.getRestriction();
			}
		};
		Page<BigAmount> page = bigAmountRepo.findAll(Specifications.where(b1).and(b2), pageable);
		Map<String, BigAmountMz> csnmSfmzMap = new HashMap<String, BigAmountMz>();
		for (BigAmount bigAmountIn : page.getContent()) {
			if (StringUtils.isNotEmpty(bigAmountIn.getCtid())) {
				if (null == csnmSfmzMap.get(bigAmountIn.getCtid())) {
					csnmSfmzMap.put(bigAmountIn.getCtid() + "_" + bigAmountIn.getCrcd(),
							bigCheckService.getBigamountSfmz(bigAmountIn));
				}
				BigAmountMz mz = csnmSfmzMap.get(bigAmountIn.getCtid() + "_" + bigAmountIn.getCrcd());
				bigAmountIn.setSfmz(mz.getSfmz());
				bigAmountIn.setHzje("");
				DecimalFormat df = new DecimalFormat("#.00");
				if (mz.getIsSrCNY()) {
					bigAmountIn.setHzje(bigAmountIn.getHzje() + "收:" + df.format(mz.getCnysrzje()) + "(CNY)");
				} else {
					bigAmountIn.setHzje(bigAmountIn.getHzje() + "收:" + df.format(mz.getUsdsrzje()) + "(USD)");
				}
				if (mz.getIsZcCNY()) {
					bigAmountIn.setHzje(bigAmountIn.getHzje() + "支:" + df.format(mz.getCnyzczje()) + "(CNY)");
				} else {
					bigAmountIn.setHzje(bigAmountIn.getHzje() + "支:" + df.format(mz.getUsdzczje()) + "(USD)");
				}
			} else {
				bigAmountIn.setSfmz("02");
			}
		}
		for (BigAmount bigAmount2 : page.getContent()) {
			String tstp = bigAmount2.getTstp();
			if (StringUtils.isNotEmpty(tstp) && tstp.length() >= 6) {
				if (tstp.substring(0, 2).equals("00")) {
					bigAmount2.setTstp(tstp.substring(0, 2) + "," + tstp.substring(2, 4) + "," + tstp.substring(4));
				} else {
					bigAmount2.setTstp(tstp.substring(0, 2) + "," + tstp.substring(2));
				}
			}
		}
		workflowMgr.fillJobsInfo(page.getContent());
		return new Resp(page);
	}

	@InFunLog(funName = "增加", args = { 0 })
	@ApiOperation(value = "添加或者更新大额信息", notes = "POST")
	@RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
	public Resp add_(@RequestBody BigAmount bigAmount) throws ParseException {
		bigAmount.setJob(null);
		bigAmount.setId(null);
		bigAmount.setDeleteState("1");
		bigAmount.setCheckType(null);
		bigAmount.setAptp("01");
		if (StringUtils.isNotEmpty(bigAmount.getTstp())) {
			String tstp = bigAmount.getTstp().replaceAll(",", "");
			bigAmount.setTstp(tstp);
		}
		if (StringUtils.isNotEmpty(bigAmount.getTrcd())) {
			String trcd = bigAmount.getTrcd().replaceAll(",", "");
			bigAmount.setTrcd(trcd);
		}
		if (StringUtils.isNotEmpty(bigAmount.getCfrc())) {
			String tdrc = bigAmount.getCfrc().replaceAll(",", "");
			bigAmount.setCfrc(tdrc);
		}
		List<BigAmount> amounts = new ArrayList<>();
		BigAmount amount = bigAmountRepo.save(bigAmount);
		amount.setStartState("PRE_SUBMIT");
		amounts.add(amount);
		workflowMgr.addJobs(config.getAmlWorkId(), amounts);
		return new Resp(amount, CodeDef.SUCCESS);
	}

	@RequestMapping(value = "/delMore", method = RequestMethod.POST)
	public Resp delMore(@RequestBody Set<Long> ids) {
		Integer count = 0;
		HashSet<Long> set = new HashSet<Long>();
		List<BigAmount> list = bigAmountRepo.findByIdIn(ids);
		String curJobState = null;
		for (BigAmount bigAmount : list) {
			curJobState = bigAmount.getFlowState();
			if (StringUtils.isEmpty(bigAmount.getTname())) {
				if (curJobState.equals("PRE_SUBMIT") || curJobState.equals("PRE_RECROD")) {
					set.add(bigAmount.getId());
				}
			}
		}
		if (set.size() > 0) {
			count = bigAmountRepo.deleteAmlBigAmount(set);
		}
		return new Resp(ids.size() - count, CodeDef.SUCCESS);
	}

	@RequestMapping(value = "/getSubmit", method = RequestMethod.POST)
	public Resp getSubmit(@RequestBody BigAmount bigAmount) throws ParseException {
		List<String> submits = null;
		if (bigAmount.getRpdt() != null) {
			submits = bigAmountRepo.getSumbit(bigAmount.getRpdt(), "PRE_EXPORTOVER");
		} else {
			submits = bigAmountRepo.getSumbit("PRE_EXPORTOVER");
		}
		return new Resp(submits);
	}

	@RequestMapping(value = "/getMores", method = RequestMethod.GET)
	public Resp getMores(@ModelAttribute PageQuery query, @ModelAttribute BigAmount bigAmount) {
		bigAmount.setDeleteState("1");
		Job job = new Job();
		job.setCurJobState("PRE_EXPORTOVER");
		bigAmount.setJob(job);
		PageRequest pageable = new PageRequest(query.getPindex(), query.getPcount(), query.getSortObj());
		Example<BigAmount> example = Example.of(bigAmount, ExampleMatcher.matchingAll());
		Page<BigAmount> page = bigAmountRepo.findAll(example, pageable);
		Map<String, BigAmountMz> csnmSfmzMap = new HashMap<String, BigAmountMz>();
		for (BigAmount bigAmountIn : page.getContent()) {
			if (StringUtils.isNotEmpty(bigAmountIn.getCtid())) {
				if (null == csnmSfmzMap.get(bigAmountIn.getCtid())) {
					try {
						csnmSfmzMap.put(bigAmountIn.getCtid() + "_" + bigAmountIn.getCrcd(),
								bigCheckService.getBigamountSfmz(bigAmountIn));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				BigAmountMz mz = csnmSfmzMap.get(bigAmountIn.getCtid() + "_" + bigAmountIn.getCrcd());
				bigAmountIn.setSfmz(mz.getSfmz());
				bigAmountIn.setHzje("");
				DecimalFormat df = new DecimalFormat("#.00");
				if (mz.getIsSrCNY()) {
					bigAmountIn.setHzje(bigAmountIn.getHzje() + "收:" + df.format(mz.getCnysrzje()) + "(CNY)");
				} else {
					bigAmountIn.setHzje(bigAmountIn.getHzje() + "收:" + df.format(mz.getUsdsrzje()) + "(USD)");
				}
				if (mz.getIsZcCNY()) {
					bigAmountIn.setHzje(bigAmountIn.getHzje() + "支:" + df.format(mz.getCnyzczje()) + "(CNY)");
				} else {
					bigAmountIn.setHzje(bigAmountIn.getHzje() + "支:" + df.format(mz.getUsdzczje()) + "(USD)");
				}
			} else {
				bigAmountIn.setSfmz("02");
			}
		}
		for (BigAmount bigAmount2 : page.getContent()) {
			String tstp = bigAmount2.getTstp();
			if (StringUtils.isNotEmpty(tstp) && tstp.length() >= 6) {
				if (tstp.substring(0, 2).equals("00")) {
					bigAmount2.setTstp(tstp.substring(0, 2) + "," + tstp.substring(2, 4) + "," + tstp.substring(4));
				} else {
					bigAmount2.setTstp(tstp.substring(0, 2) + "," + tstp.substring(2));
				}
			}
		}
		workflowMgr.fillJobsInfo(page.getContent());
		return new Resp(page);
	}

	@RequestMapping(value = "/reset", method = RequestMethod.POST)
	public Resp reSet(@RequestBody BigAmount bigAmount) throws JsonProcessingException {
		if (bigAmount.getRpdt() == null || StringUtils.isEmpty(bigAmount.getNowSubmitBatch())) {
			return new Resp("", CodeDef.ERROR);
		}
		String userName = TokenHolder.get().getUserName();
		Set<Long> ids = bigAmountRepo.findIdBySubitBatch(bigAmount.getRpdt(), bigAmount.getNowSubmitBatch(),
				"PRE_EXPORTOVER");
		List<BigAmount> bigAmounts = bigAmountRepo.findByIdIn(ids);
		Set<JobEvent> ets = new HashSet<>();
		for (int i = 0; i < bigAmounts.size(); i++) {
			JobEvent jobEvent = new JobEvent(bigAmounts.get(i).getJobId(), "RESET", userName);
			jobEvent.setFireEvent(false);
			ets.add(jobEvent);
		}
		int count = 0;
		List<EventResult> list = workflowMgr.sendEvents(ets);
		for (EventResult eventResult : list) {
			if (eventResult.isSuccess()) {
				count++;
			}
		}
		return new Resp(list.size() - count);
	}

	@RequestMapping(value = "/reportAgain", method = RequestMethod.POST)
	public Resp reportAgain(@RequestBody BigAmount bigAmount) throws JsonProcessingException {
		if (bigAmount.getRpdt() == null || StringUtils.isEmpty(bigAmount.getNowSubmitBatch())) {
			return new Resp("", CodeDef.ERROR);
		}
		String userName = TokenHolder.get().getUserName();
		Set<Long> ids = bigAmountRepo.findIdBySubitBatch(bigAmount.getRpdt(), bigAmount.getNowSubmitBatch(),
				"PRE_EXPORTOVER");
		List<BigAmount> bigAmounts = bigAmountRepo.findByIdIn(ids);
		//workflowMgr.fillJobsInfo(bigAmounts);
		Set<JobEvent> ets = new HashSet<>();
		for (int i = 0; i < bigAmounts.size(); i++) {
			bigAmounts.get(i).setAptp("01");
			JobEvent jobEvent = new JobEvent(bigAmounts.get(i).getJobId(), "REPORT_AGAIN", userName);
			jobEvent.setFireEvent(false);
//			bigAmounts.get(i).setJob(null);
//			jobEvent.setTargetJson(mapper.writeValueAsString(bigAmounts.get(i)));
			ets.add(jobEvent);
		}
		int count = 0;
		List<EventResult> results = workflowMgr.sendEvents(ets);
		for (EventResult eventResult : results) {
			if (eventResult.isSuccess()) {
				count++;
			}
		}
		return new Resp(results.size()-count, CodeDef.SUCCESS);
	}

	@RequestMapping(value = "/addStorageMore", method = RequestMethod.POST)
	public Resp addStorageMore(@RequestBody BigAmount bigAmount) throws JsonProcessingException {
		if (bigAmount.getRpdt() == null || StringUtils.isEmpty(bigAmount.getNowSubmitBatch())) {
			return new Resp("", CodeDef.ERROR);
		}
		String userName = TokenHolder.get().getUserName();
		Set<Long> ids = bigAmountRepo.findIdBySubitBatch(bigAmount.getRpdt(), bigAmount.getNowSubmitBatch(),
				"PRE_EXPORTOVER");
		List<BigAmount> bigAmounts = bigAmountRepo.findByIdIn(ids);
		//workflowMgr.fillJobsInfo(bigAmounts);
		Set<JobEvent> ets = new HashSet<>();
		for (int i = 0; i < bigAmounts.size(); i++) {
			JobEvent jobEvent = new JobEvent(bigAmounts.get(i).getJobId(), "ADD_STORAGE", userName);
			jobEvent.setFireEvent(false);
//			bigAmounts.get(i).setJob(null);
//			jobEvent.setTargetJson(mapper.writeValueAsString(bigAmounts.get(i)));
			ets.add(jobEvent);
		}
		int count = 0;
		List<EventResult> list = workflowMgr.sendEvents(ets);
		for (EventResult eventResult : list) {
			if (eventResult.isSuccess()) {
				count++;
			}
		}
		return new Resp(list.size() - count ,CodeDef.SUCCESS);
	}

	@RequestMapping(value = "/getInit", method = RequestMethod.GET)
	public Resp getInit() throws Exception {
		BigAmount bigAmount = new BigAmount();
		bigAmount.setReportType("N");
		bigAmount.setBwzt("xzbw");
		bigAmount.setSubmitType("Y");
		bigAmount.setOitp1("@N");
		bigAmount.setOitp2("@N");
		bigAmount.setOitp3("@N");
		bigAmount.setOoct("@N");
		bigAmount.setOcbt("@N");
		bigAmount.setRotf1("@N");
		bigAmount.setRotf2("@N");
		bigAmount.setMirs("@N");
		Long jgid = TokenHolder.get().getBranchId();
		if (null != jgid) {
			Branch branch = userBranchServiceImpl.getBranch(TokenHolder.get().getBranchId(),
					TokenHolder.get().getToken());
			if (null != branch) {
				bigAmount.setJgbm(branch.getBranchCode());
				bigAmount.setFinc(branch.getAmlBranchCode());
				bigAmount.setRicd(branch.getAmlBranchCode());
			}
		}
		bigAmount.setRpdt(new Date());
		return new Resp(bigAmount, CodeDef.SUCCESS);
	}

	@RequestMapping(value = "/check", method = RequestMethod.POST)
	public Resp check(@RequestBody BigAmount bigAmount) throws Exception {
		if (StringUtils.isNotEmpty(bigAmount.getTstp())) {
			String tstp = bigAmount.getTstp().replaceAll(",", "");
			bigAmount.setTstp(tstp);
		}
		if (StringUtils.isNotEmpty(bigAmount.getTrcd())) {
			String trcd = bigAmount.getTrcd().replaceAll(",", "");
			bigAmount.setTrcd(trcd);
		}
		if (StringUtils.isNotEmpty(bigAmount.getCfrc())) {
			String cfrc = bigAmount.getCfrc().replaceAll(",", "");
			bigAmount.setCfrc(cfrc);
		}
		CheckResult result = bigSuspCheckService.checkBigAmount(bigAmount, false);
		return new Resp(result, CodeDef.SUCCESS);
	}

	@RequestMapping(value = "/getCsnm", method = RequestMethod.POST)
	public Resp getCsnm(@RequestBody BigAmount bigAmount) throws ParseException {
		List<String> csnm = bigAmountRepo.getCsnm(bigAmount.getRpdt(), "PRE_STORAGE");
		return new Resp(csnm);
	}

	@RequestMapping(value = "/getMoreForDel", method = RequestMethod.GET)
	public Resp getMoreForDel(@ModelAttribute PageQuery query, @ModelAttribute BigAmount bigAmount) {
		PageRequest pageable = new PageRequest(query.getPindex(), query.getPcount(), query.getSortObj());
		Page<BigAmount> page = null;
		page = bigAmountRepo.findAll(new Specification<BigAmount>() {
			@Override
			public Predicate toPredicate(Root<BigAmount> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				if (bigAmount.getRpdt() != null) {
					list.add(cb.equal((root.get("rpdt").as(Date.class)), bigAmount.getRpdt()));
				}
				if (StringUtils.isNotEmpty(bigAmount.getCrcd())) {
					list.add(cb.equal((root.get("crcd").as(String.class)), bigAmount.getCrcd()));
				}
				if (StringUtils.isNotEmpty(bigAmount.getCsnm())) {
					list.add(cb.equal((root.get("csnm").as(String.class)), bigAmount.getCsnm()));
				}
				list.add(cb.equal((root.get("deleteState").as(String.class)), "1"));
//				Join<BigAmount, Job> depJoin = root.join(root.getModel().getSingularAttribute("job", Job.class),
//						JoinType.LEFT);
				list.add(cb.equal(root.get("flowState").as(String.class), "PRE_STORAGE"));
				list.add(cb.notEqual(root.get("reportType").as(String.class), "D"));
				Predicate[] p = new Predicate[list.size()];
				query.where(cb.and(list.toArray(p)));
				return query.getRestriction();
			}
		}, pageable);
		Map<String, BigAmountMz> csnmSfmzMap = new HashMap<String, BigAmountMz>();
		for (BigAmount bigAmountIn : page.getContent()) {
			if (null == csnmSfmzMap.get(bigAmountIn.getCtid())) {
				try {
					csnmSfmzMap.put(bigAmountIn.getCtid() + "_" + bigAmountIn.getCrcd(),
							bigCheckService.getBigamountSfmz(bigAmountIn));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			BigAmountMz mz = csnmSfmzMap.get(bigAmountIn.getCtid() + "_" + bigAmountIn.getCrcd());
			bigAmountIn.setSfmz(mz.getSfmz());
			bigAmountIn.setHzje("");
			DecimalFormat df = new DecimalFormat("#.00");
			if (mz.getIsSrCNY()) {
				bigAmountIn.setHzje(bigAmountIn.getHzje() + "收:" + df.format(mz.getCnysrzje()) + "(CNY)");
			} else {
				bigAmountIn.setHzje(bigAmountIn.getHzje() + "收:" + df.format(mz.getUsdsrzje()) + "(USD)");
			}
			if (mz.getIsZcCNY()) {
				bigAmountIn.setHzje(bigAmountIn.getHzje() + "支:" + df.format(mz.getCnyzczje()) + "(CNY)");
			} else {
				bigAmountIn.setHzje(bigAmountIn.getHzje() + "支:" + df.format(mz.getUsdzczje()) + "(USD)");
			}
		}
		for (BigAmount bigAmount2 : page.getContent()) {
			String tstp = bigAmount2.getTstp();
			if (StringUtils.isNotEmpty(tstp) && tstp.length() >= 6) {
				if (tstp.substring(0, 2).equals("00")) {
					bigAmount2.setTstp(tstp.substring(0, 2) + "," + tstp.substring(2, 4) + "," + tstp.substring(4));
				} else {
					bigAmount2.setTstp(tstp.substring(0, 2) + "," + tstp.substring(2));
				}
			}
		}
		workflowMgr.fillJobsInfo(page.getContent());
		return new Resp(page, CodeDef.SUCCESS);
	}

	@InFunLog(funName = "批量删除", args = { 0 })
	@RequestMapping(value = "/delReport", method = RequestMethod.POST)
	public Resp delReport(@RequestBody BigAmount bigAmount) throws JsonProcessingException {
		String userName = TokenHolder.get().getUserName();
		String bwzt = "sjsc";
		if (StringUtils.isNotEmpty(bigAmount.getCsnm())) {
			// 特征不为空
			if (StringUtils.isNotEmpty(bigAmount.getCrcd())) {
				bwzt = "tzsc";
			} else {
				bwzt = "khsc";
			}
		}
		List<BigAmount> bigAmounts = bigAmountRepo.findAll(new Specification<BigAmount>() {
			@Override
			public Predicate toPredicate(Root<BigAmount> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				if (bigAmount.getRpdt() != null) {
					list.add(cb.equal((root.get("rpdt").as(Date.class)), bigAmount.getRpdt()));
				}
				if (StringUtils.isNotEmpty(bigAmount.getCrcd())) {
					list.add(cb.equal((root.get("crcd").as(String.class)), bigAmount.getCrcd()));
				}
				if (StringUtils.isNotEmpty(bigAmount.getCsnm())) {
					list.add(cb.equal((root.get("csnm").as(String.class)), bigAmount.getCsnm()));
				}
				list.add(cb.equal((root.get("deleteState").as(String.class)), "1"));
				Join<BigAmount, Job> depJoin = root.join(root.getModel().getSingularAttribute("job", Job.class),
						JoinType.LEFT);
				list.add(cb.equal(depJoin.get("curJobState").as(String.class), "PRE_STORAGE"));
				list.add(cb.notEqual(root.get("reportType").as(String.class), "D"));
				Predicate[] p = new Predicate[list.size()];
				query.where(cb.and(list.toArray(p)));
				return query.getRestriction();
			}
		});
		//workflowMgr.fillJobsInfo(bigAmounts);
		Set<JobEvent> ets = new HashSet<>();
		for (int i = 0; i < bigAmounts.size(); i++) {
			bigAmounts.get(i).setReportType("D");
			bigAmounts.get(i).setBwzt(bwzt);
			bigAmounts.get(i).setAptp("01");
			JobEvent jobEvent = new JobEvent(bigAmounts.get(i).getJobId(), "DELETE", userName);
			jobEvent.setFireEvent(false);
			//jobEvent.setTargetJson(mapper.writeValueAsString(bigAmounts.get(i)));
			ets.add(jobEvent);
		}
		int count = 0;
		List<EventResult> list = workflowMgr.sendEvents(ets);
		for (EventResult eventResult : list) {
			if (eventResult.isSuccess()) {
				count++;
			}
		}
		return new Resp(list.size()-count, CodeDef.SUCCESS);
	}

	@RequestMapping(value = "/findOne", method = RequestMethod.GET)
	public Resp findOne(@RequestParam("tcac") String tcac) {
		HashSet<String> status = new HashSet<String>();
		status.add("PRE_VERIFY");
		status.add("PRE_EXPORT");
		status.add("PRE_EXPORTOVER");
		status.add("PRE_STORAGE");
		List<BigAmount> list = bigAmountRepo.findAll(new Specification<BigAmount>() {
			@Override
			public Predicate toPredicate(Root<BigAmount> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				list.add(cb.equal((root.get("deleteState").as(String.class)), "1"));
//				Join<BigAmount, Job> depJoin = root.join(root.getModel().getSingularAttribute("job", Job.class),
//						JoinType.LEFT);
				list.add(root.get("flowState").as(String.class).in(status));
				list.add(cb.equal(root.get("tcac").as(String.class), tcac));
				// 非超级管理员
				if (null != TokenHolder.get().getBranchId()) {
					List<String> branchCodes = userBranchServiceImpl.get(TokenHolder.get().getUserId());
					if (branchCodes != null && branchCodes.size() > 0) {
						list.add(root.get("jgbm").as(String.class).in(branchCodes));
					}
				}
				Predicate[] p = new Predicate[list.size()];
				query.where(cb.and(list.toArray(p)));
				query.orderBy(cb.desc(root.get("rpdt").as(Date.class)));  
				return query.getRestriction();
			}
		});
		BigAmount bigAmount = new BigAmount();
		bigAmount.setCfin("");
		bigAmount.setCfic("");
		bigAmount.setCfct("");
		bigAmount.setCfrc("");
		bigAmount.setTcnm("");
		bigAmount.setTcac(tcac);
		bigAmount.setTcat("");
		bigAmount.setTcit("");
		bigAmount.setTcid("");
		bigAmount.setOitp3("");
		if(list.size()>0){
			bigAmount= list.get(0);
		}
		bigAmount.setJob(null);
		return new Resp(bigAmount, CodeDef.SUCCESS);
	}
	
}

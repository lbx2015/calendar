package net.riking.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.config.Config;
import net.riking.core.annos.AuthPass;
import net.riking.core.entity.JobEvent;
import net.riking.core.entity.MultipleChoiceCustom;
import net.riking.core.entity.Resp;
import net.riking.core.entity.TokenHolder;
import net.riking.core.log.InFunLog;
import net.riking.core.log.InModLog;
import net.riking.core.workflow.EventResult;
import net.riking.core.workflow.WorkflowMgr;
import net.riking.entity.PageQuery;
import net.riking.entity.model.AmlRuleEngine;
import net.riking.entity.model.AmlSuspicious;
import net.riking.entity.model.BaseAif;
import net.riking.entity.model.BaseCorpCust;
import net.riking.entity.model.BaseIndvCust;
import net.riking.entity.model.BaseTrn;
import net.riking.entity.model.Branch;
import net.riking.entity.model.CheckResult;
import net.riking.entity.model.SusAttachment;
import net.riking.service.BigSuspCheckServiceImpl;
import net.riking.service.UserBranchServiceImpl;
import net.riking.service.repo.AmlRuleEngineRepo;
import net.riking.service.repo.AmlSuspiciousRepo;
import net.riking.service.repo.BaseAifRepo;
import net.riking.service.repo.BaseCorpCustRepo;
import net.riking.service.repo.BaseIndvCustRepo;
import net.riking.service.repo.BaseTrnRepo;
import net.riking.service.repo.SusAttachmentRepo;
import net.riking.util.ExcelToList;

@InModLog(modName = "可疑交易报送")
@RestController
@RequestMapping(value = "/suspiciouss")
public class SuspiciousController {
	@Autowired
	Config config;

	@Autowired
	WorkflowMgr workflowMgr;

	@Autowired
	ObjectMapper mapper = new ObjectMapper();

	@Autowired
	AmlSuspiciousRepo suspiciousRepo;

	@Autowired
	AmlRuleEngineRepo amlRuleEngineRepo;

	@Autowired
	BigSuspCheckServiceImpl bigSuspCheckService;

	@Autowired
	BaseAifRepo baseAifRepo;

	@Autowired
	BaseCorpCustRepo baseCorpCustRepo;

	@Autowired
	BaseTrnRepo baseTrnRepo;

	@Autowired
	BaseIndvCustRepo baseIndvCustRepo;
	@Autowired
	SusAttachmentRepo susAttachmentRepo;
	@Autowired
	UserBranchServiceImpl userBranchServiceImpl;

	// @InitBinder
	// protected void initBinder(WebDataBinder binder) {
	// binder.registerCustomEditor(Date.class, new CustomDateEditor(new
	// SimpleDateFormat("yyyy-MM-dd"), true));
	// }

	// 规则: 查,删 操作接口使用RequestMethod.GET，失败情况可以重复请求
	// 增，改使用RequestMethod.POST，不能重复请求
	// 为降低难度与兼容性， DELETE,PUT等操作不用。

	@InFunLog(funName = "增加", args = { 0 })
	@ApiOperation(value = "添加或者更新可疑交易信息", notes = "POST-@Suspicious")
	@RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
	public Resp add_(@RequestBody AmlSuspicious suspicious) throws JsonProcessingException {
		suspicious.setJob(null);
		suspicious.setId(null);
		suspicious.setDeleteState("1");
		suspicious.setAptp("01");
		suspicious.setCheckType(null);
		if (StringUtils.isNotEmpty(suspicious.getCfrc())) {
			String getCfrc = suspicious.getCfrc().replaceAll(",", "");
			suspicious.setCfrc(getCfrc);
		}
		if (StringUtils.isNotEmpty(suspicious.getTstp())) {
			String tstp = suspicious.getTstp().replaceAll(",", "");
			suspicious.setTstp(tstp);
		}
		if (StringUtils.isNotEmpty(suspicious.getTrcd())) {
			String trcd = suspicious.getTrcd().replaceAll(",", "");
			suspicious.setTrcd(trcd);
		}
		AmlSuspicious suspiciou = suspiciousRepo.save(suspicious);
		suspiciou.setStartState("PRE_SUBMIT");
		workflowMgr.addJobs(config.getAmlWorkId(), Arrays.asList(suspiciou));
		return new Resp(suspiciou, CodeDef.SUCCESS);
	}

	/*
	 * @InFunLog(funName = "删除", args = { 0 })
	 * 
	 * @ApiOperation(value = "删除可疑交易", notes = "根据url的id来指定删除对象")
	 * 
	 * @ApiImplicitParam(name = "id", value = "可疑交易ID", required = true,
	 * dataType = "Long")
	 * 
	 * @RequestMapping(value = "/del", method = RequestMethod.GET) public Resp
	 * del_(@RequestParam("id") Long id) { suspiciousRepo.delete(id); return new
	 * Resp(1); }
	 */
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public Resp get_(@RequestParam("id") Long id) {
		AmlSuspicious suspicious = suspiciousRepo.findOne(id);
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

		workflowMgr.fillJobsInfo(Arrays.asList(suspicious));
		return new Resp(suspicious, CodeDef.SUCCESS);
	}

	@RequestMapping(value = "/getes", method = RequestMethod.GET)
	public Resp getes_(@RequestParam("id") Long id) {
		AmlSuspicious suspicious = suspiciousRepo.findOne(id);
		suspicious.setTicd(null);
		suspicious.setJob(null);
		suspicious.setId(null);
		suspicious.setTname(null);
		suspicious.setSubmitBatch(null);
		suspicious.setNowSubmitBatch(null);
		suspicious.setJobId(null);
		suspicious.setFlowState(null);
		suspicious.setFlowOwner(null);
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

	@AuthPass
	@RequestMapping(value = "/addMore", method = RequestMethod.POST)
	public Resp addMore(HttpServletRequest request) {
		String branchCode = request.getParameter("branchCode");
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile mFile = multipartRequest.getFile("bw");
		String fileName = mFile.getOriginalFilename();
		String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
		List<AmlSuspicious> list = null;
		try {
			InputStream is = mFile.getInputStream();
			if (suffix.equals("xlsx")) {
				list = ExcelToList.readsusXlsx(is, fileName);
			} else {
				list = ExcelToList.readsusXls(is, fileName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int count = 0;// 插入的数量
		String sum = "";// 记录重复且忽略的
		int j = 0;
		ArrayList<AmlSuspicious> addList = new ArrayList<AmlSuspicious>();// 保存可以替换的
		Set<Long> delSet = new HashSet<Long>();// 保存要被替换的id
		Date start = null;
		Date end = null;
		SimpleDateFormat sdf = new SimpleDateFormat();
		for (int i = 0; i < list.size(); i++) {
			j++;
			AmlSuspicious suspiciou = list.get(i);
			// 初始化jgbm
			suspiciou.setJgbm(branchCode);
			if (suspiciou.getTicd() == null || suspiciou.getTstm() == null) {
				return new Resp("第" + j + "条数据，业务标识号或交易日期不完整", CodeDef.ERROR);
			}
			Date date = DateUtils.addDays(suspiciou.getTstm(), 1);
			try {
				start = sdf.parse(sdf.format(suspiciou.getTstm()));
				end = sdf.parse(sdf.format(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			// 判断ticd（业务标识号）是否重复
			List<AmlSuspicious> otherList = suspiciousRepo.findByTicdAndTstmBetweenAndDeleteState(suspiciou.getTicd(),
					start, end, "1");
			if (otherList.size() == 0) {// 未重复
				continue;
			}
			list.remove(i);// 将重复的 删除
			i--;
			// 重复
			workflowMgr.fillJobsInfo(otherList);
			String curJobState = otherList.get(0).getFlowState();
			// 当前状态在审核之前直接删掉再添加 若为审核之后 ，忽略这次记录
			if (!(curJobState.equals("SISP_PRE_DEAL") || curJobState.equals("SISP_PRE_VERIFY")
					|| curJobState.equals("PRE_RECROD") || curJobState.equals("PRE_SUBMIT"))) {
				sum = j + "，";
			} else {
				delSet.add(otherList.get(0).getId());
				addList.add(suspiciou);
			}
		}
		if (addList.size() > 0) {// 可以替换的
			suspiciousRepo.deleteAmlSuspicious(delSet);
			for (AmlSuspicious suspiciou : addList) {
				this.initData(suspiciou);
				AmlSuspicious suspicious = suspiciousRepo.save(suspiciou);
				suspicious.setStartState("PRE_RECROD");
				workflowMgr.addJobs(config.getAmlWorkId(), Arrays.asList(suspicious));
				count++;
			}
		}
		if (list.size() > 0) {// 判断有不重复的，且将其保存
			for (int i = 0; i < list.size(); i++) {
				AmlSuspicious suspiciou = list.get(i);
				suspiciou.setDeleteState("1");
				suspiciou.setAptp("01");
				AmlSuspicious suspicious = suspiciousRepo.save(suspiciou);
				suspicious.setStartState("PRE_RECROD");
				workflowMgr.addJobs(config.getAmlWorkId(), Arrays.asList(suspicious));
				count++;
			}
		}
		return new Resp("有" + count + "条数据导入成功。" + (StringUtils.isEmpty(sum) ? "" : "第" + sum + "条记录重复，导入失败。"),
				CodeDef.SUCCESS);
	}

	@RequestMapping(value = "/delMore", method = RequestMethod.POST)
	public Resp delMore(@RequestBody Set<Long> ids) {
		Integer count = 0;
		List<AmlSuspicious> list = suspiciousRepo.findByIdIn(ids);
		HashSet<Long> set = new HashSet<Long>();
		String curJobState = null;
		for (AmlSuspicious amlSuspicious : list) {
			curJobState = amlSuspicious.getFlowState();
			if (StringUtils.isEmpty(amlSuspicious.getTname())) {
				if (curJobState.equals("PRE_SUBMIT") || curJobState.equals("PRE_RECROD")) {
					set.add(amlSuspicious.getId());
				}
			}
		}
		if (set.size() > 0) {
			count = suspiciousRepo.deleteAmlSuspicious(set);
		}
		return new Resp(ids.size() - count, CodeDef.SUCCESS);
	}

	@RequestMapping(value = "/getMore", method = RequestMethod.GET)
	public Resp getMore(@ModelAttribute PageQuery query, @ModelAttribute AmlSuspicious su) {
		PageRequest pageable = new PageRequest(query.getPindex(), query.getPcount(), query.getSortObj());
		Specification<AmlSuspicious> s1 = null;
		if (su.getJob() != null && su.getJob().getCurJobState() != null) {
			s1 = new Specification<AmlSuspicious>() {
				@Override
				public Predicate toPredicate(Root<AmlSuspicious> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					List<Predicate> list = new ArrayList<Predicate>();
//					Join<AmlSuspicious, Job> depJoin = root.join(root.getModel().getSingularAttribute("job", Job.class),
//							JoinType.LEFT);
					list.add(cb.equal((root.get("flowState").as(String.class)), su.getJob().getCurJobState()));
					Predicate[] p = new Predicate[list.size()];
					return cb.or(list.toArray(p));
				}
			};
		} else {
			s1 = new Specification<AmlSuspicious>() {

				@Override
				public Predicate toPredicate(Root<AmlSuspicious> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					List<Predicate> list = new ArrayList<Predicate>();
//					Join<AmlSuspicious, Job> depJoin = root.join(root.getModel().getSingularAttribute("job", Job.class),
//							JoinType.LEFT);
					if (su.getUpState() != null && su.getUpState().equals("NoUp")) {
						list.add(cb.equal((root.get("flowState").as(String.class)), "SISP_PRE_DEAL"));
						list.add(cb.equal((root.get("flowState").as(String.class)), "SISP_PRE_VERIFY"));
						list.add(cb.equal((root.get("flowState").as(String.class)), "PRE_RECROD"));
						list.add(cb.equal((root.get("flowState").as(String.class)), "PRE_SUBMIT"));
						list.add(cb.equal((root.get("flowState").as(String.class)), "PRE_VERIFY"));
						list.add(cb.equal((root.get("flowState").as(String.class)), "PRE_EXPORT"));
					} else if (su.getUpState() != null && su.getUpState().equals("Up")) {
						list.add(cb.equal((root.get("flowState").as(String.class)), "PRE_EXPORTOVER"));
						list.add(cb.equal((root.get("flowState").as(String.class)), "PRE_STORAGE"));
					} else {
						list.add(cb.equal((root.get("flowState").as(String.class)), "SISP_PRE_DEAL"));
						list.add(cb.equal((root.get("flowState").as(String.class)), "SISP_PRE_VERIFY"));
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

		Specification<AmlSuspicious> s2 = new Specification<AmlSuspicious>() {
			@Override
			public Predicate toPredicate(Root<AmlSuspicious> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				if (null != su.getCsnm1()) {
					list.add(cb.equal((root.get("csnm1").as(String.class)), su.getCsnm1()));
				}
				if (su.getCtnm() != null) {
					list.add(cb.like((root.get("ctnm").as(String.class)), "%" + su.getCtnm() + "%"));
				}
				if (su.getTicd() != null) {
					list.add(cb.like((root.get("ticd").as(String.class)), "%" + su.getTicd() + "%"));
				}
				if (su.getStarts() != null || su.getEnds() != null) {
					if (su.getEnds() == null) {
						if (su.getQueryDate() != null && su.getQueryDate().equals("tstm")) {
							list.add(cb.greaterThanOrEqualTo(root.<Date> get("tstm"), su.getStarts()));
						} else {
							list.add(cb.greaterThanOrEqualTo(root.<Date> get("rpdt"), su.getStarts()));
						}
					} else if (su.getStarts() == null) {
						if (su.getQueryDate() != null && su.getQueryDate().equals("tstm")) {
							Long time = su.getEnds().getTime() + 24 * 60 * 60 * 1000L - 1L;
							list.add(cb.lessThanOrEqualTo(root.<Date> get("tstm"), new Date(time)));
						} else {
							list.add(cb.lessThanOrEqualTo(root.<Date> get("rpdt"), su.getEnds()));
						}
					} else {
						if (su.getQueryDate() != null && su.getQueryDate().equals("tstm")) {
							Long time = su.getEnds().getTime() + 24 * 60 * 60 * 1000L - 1L;
							list.add(cb.between(root.<Date> get("tstm"), su.getStarts(), new Date(time)));
						} else {
							list.add(cb.between(root.<Date> get("rpdt"), su.getStarts(), su.getEnds()));
						}
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
		Page<AmlSuspicious> page = suspiciousRepo.findAll(Specifications.where(s1).and(s2), pageable);
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

	@RequestMapping(value = "/addStorage", method = RequestMethod.POST)
	public Resp addStorage(@RequestBody Set<Long> ids) throws JsonProcessingException {
		String userName = TokenHolder.get().getUserName();
		Integer count = 0;
		Set<JobEvent> ets = new HashSet<JobEvent>();
		List<AmlSuspicious> suspicious = suspiciousRepo.findByIdIn(ids);
		for (int i = 0; i < suspicious.size(); i++) {
			JobEvent jobEvent = new JobEvent(suspicious.get(i).getJobId(), "ADD_STORAGE", userName);
			jobEvent.setTargetJson(mapper.writeValueAsString(suspicious.get(i)));
			ets.add(jobEvent);
			count++;
		}
		workflowMgr.sendEvents(ets);
		return new Resp(count);
	}

	@RequestMapping(value = "/getSubmit", method = RequestMethod.POST)
	public Resp getSubmit(@RequestBody AmlSuspicious suspicious) throws ParseException {
		List<String> Submit = suspiciousRepo.getSumbit(suspicious.getRpdt(), "PRE_EXPORTOVER");
		return new Resp(Submit);
	}

	@RequestMapping(value = "/getMores", method = RequestMethod.GET)
	public Resp getMores(@ModelAttribute PageQuery query, @ModelAttribute AmlSuspicious suspicious) {
		suspicious.setDeleteState("1");
//		Job job = new Job();
//		job.setCurJobState("PRE_EXPORTOVER");
//		suspicious.setJob(job);
		suspicious.setFlowState("PRE_EXPORTOVER");
		PageRequest pageable = new PageRequest(query.getPindex(), query.getPcount(), query.getSortObj());
		Example<AmlSuspicious> example = Example.of(suspicious, ExampleMatcher.matchingAll());
		Page<AmlSuspicious> page = suspiciousRepo.findAll(example, pageable);
		for (AmlSuspicious susp : page.getContent()) {
			String tstp = susp.getTstp();
			if (StringUtils.isNotEmpty(tstp) && tstp.length() >= 6) {
				if (tstp.substring(0, 2).equals("00")) {
					susp.setTstp(tstp.substring(0, 2) + "," + tstp.substring(2, 4) + "," + tstp.substring(4));
				} else {
					susp.setTstp(tstp.substring(0, 2) + "," + tstp.substring(2));
				}
			}
		}
		workflowMgr.fillJobsInfo(page.getContent());
		return new Resp(page);
	}

	@RequestMapping(value = "/reset", method = RequestMethod.POST)
	public Resp reSet(@RequestBody AmlSuspicious suspicious) throws JsonProcessingException {
		if (suspicious.getRpdt() == null || StringUtils.isEmpty(suspicious.getNowSubmitBatch())) {
			return new Resp("", CodeDef.ERROR);
		}
		String userName = TokenHolder.get().getUserName();
		Set<Long> ids = suspiciousRepo.findIdBySubitBatch(suspicious.getRpdt(), suspicious.getNowSubmitBatch(),
				"PRE_EXPORTOVER");
		List<AmlSuspicious> suspiciouss = suspiciousRepo.findByIdIn(ids);
		Set<JobEvent> ets = new HashSet<>();
//		workflowMgr.fillJobsInfo(suspiciouss);
		for (int i = 0; i < suspiciouss.size(); i++) {
			JobEvent jobEvent = new JobEvent(suspiciouss.get(i).getJobId(), "RESET", userName);
			jobEvent.setFireEvent(false);
//			jobEvent.setTargetJson(mapper.writeValueAsString(suspiciouss.get(i)));
			ets.add(jobEvent);
		}
		int count = 0;
		List<EventResult> list = workflowMgr.sendEvents(ets);
		for (EventResult eventResult : list) {
			if (eventResult.isSuccess()) {
				count++;
			}
		}
		return new Resp(list.size() - count, CodeDef.SUCCESS);
	}

	@RequestMapping(value = "/reportAgain", method = RequestMethod.POST)
	public Resp reportAgain(@RequestBody AmlSuspicious suspicious) throws JsonProcessingException {
		if (suspicious.getRpdt() == null || StringUtils.isEmpty(suspicious.getNowSubmitBatch())) {
			return new Resp("", CodeDef.ERROR);
		}
		String userName = TokenHolder.get().getUserName();
		Set<Long> ids = suspiciousRepo.findIdBySubitBatch(suspicious.getRpdt(), suspicious.getNowSubmitBatch(),
				"PRE_EXPORTOVER");
		List<AmlSuspicious> suspiciouss = suspiciousRepo.findByIdIn(ids);
		Set<JobEvent> ets = new HashSet<JobEvent>();
//		workflowMgr.fillJobsInfo(suspiciouss);
		for (int i = 0; i < suspiciouss.size(); i++) {
			suspiciouss.get(i).setAptp("01");
			JobEvent jobEvent = new JobEvent(suspiciouss.get(i).getJobId(), "REPORT_AGAIN", userName);
			jobEvent.setFireEvent(false);
//			jobEvent.setTargetJson(mapper.writeValueAsString(suspiciouss.get(i)));
			ets.add(jobEvent);
		}
		int count = 0;
		List<EventResult> list = workflowMgr.sendEvents(ets);
		for (EventResult eventResult : list) {
			if (eventResult.isSuccess()) {
				count++;
			}
		}
		return new Resp(list.size() - count, CodeDef.SUCCESS);
	}

	@RequestMapping(value = "/addStorageMore", method = RequestMethod.POST)
	public Resp addStorageMore(@RequestBody AmlSuspicious suspicious) throws JsonProcessingException {
		if (suspicious.getRpdt() == null || StringUtils.isEmpty(suspicious.getNowSubmitBatch())) {
			return new Resp("", CodeDef.ERROR);
		}
		String userName = TokenHolder.get().getUserName();
		Set<Long> ids = suspiciousRepo.findIdBySubitBatch(suspicious.getRpdt(), suspicious.getNowSubmitBatch(),
				"PRE_EXPORTOVER");
		List<AmlSuspicious> suspiciouss = suspiciousRepo.findByIdIn(ids);
		Set<JobEvent> ets = new HashSet<>();
//		workflowMgr.fillJobsInfo(suspiciouss);
		for (int i = 0; i < suspiciouss.size(); i++) {
			JobEvent jobEvent = new JobEvent(suspiciouss.get(i).getJobId(), "ADD_STORAGE", userName);
			jobEvent.setFireEvent(false);
//			jobEvent.setTargetJson(mapper.writeValueAsString(suspiciouss.get(i)));
			ets.add(jobEvent);
		}
		int count = 0;
		List<EventResult> list = workflowMgr.sendEvents(ets);
		for (EventResult eventResult : list) {
			if (eventResult.isSuccess()) {
				count++;
			}
		}
		return new Resp(list.size() - count, CodeDef.SUCCESS);
	}

	@RequestMapping(value = "/check", method = RequestMethod.POST)
	public Resp check(@RequestBody AmlSuspicious amlSuspicious) throws Exception {
		if (StringUtils.isNotEmpty(amlSuspicious.getCfrc())) {
			String getCfrc = amlSuspicious.getCfrc().replaceAll(",", "");
			amlSuspicious.setCfrc(getCfrc);
		}
		if (StringUtils.isNotEmpty(amlSuspicious.getTstp())) {
			String tstp = amlSuspicious.getTstp().replaceAll(",", "");
			amlSuspicious.setTstp(tstp);
		}
		if (StringUtils.isNotEmpty(amlSuspicious.getTrcd())) {
			String trcd = amlSuspicious.getTrcd().replaceAll(",", "");
			amlSuspicious.setTrcd(trcd);
		}
		CheckResult result = bigSuspCheckService.checkSuspicious(amlSuspicious, false);
		return new Resp(result, CodeDef.SUCCESS);
	}

	// 得到可疑特征
	@RequestMapping(value = "/getStcr", method = RequestMethod.GET)
	public Resp getStcr(@RequestParam(value = "prop", required = false) String prop) throws Exception {
		List<AmlRuleEngine> list = amlRuleEngineRepo.findByType(2);
		MultipleChoiceCustom choice;
		List<MultipleChoiceCustom> multipleChoiceCustoms = new ArrayList<MultipleChoiceCustom>();
		for (AmlRuleEngine rule : list) {
			choice = new MultipleChoiceCustom();
			choice.setKey(rule.getRuleNo());
			choice.setValue(rule.getRuleName());
			choice.setProp(prop);
			multipleChoiceCustoms.add(choice);
		}
		return new Resp(multipleChoiceCustoms, CodeDef.SUCCESS);
	}

	@RequestMapping(value = "/getInit", method = RequestMethod.GET)
	public Resp getInit() throws Exception {
		AmlSuspicious aml = new AmlSuspicious();
		aml.setReportType("N");
		aml.setBwzt("xzbw");
		aml.setSubmitType("Y");
		aml.setTorp("1");
		aml.setOrxn("@N");
		aml.setOitp1("@N");
		aml.setOitp2("@N");
		aml.setOitp3("@N");
		aml.setOitp4("@N");
		aml.setOrit("@N");
		aml.setOcit("@N");
		aml.setOcbt("@N");
		aml.setOoct("@N");
		aml.setRotf1("@N");
		aml.setRotf2("@N");
		aml.setOdrp("@N");
		aml.setOtpr("@N");
		aml.setMirs("@N");
		Long jgid = TokenHolder.get().getBranchId();
		if (null != jgid) {
			Branch branch = userBranchServiceImpl.getBranch(TokenHolder.get().getBranchId(),
					TokenHolder.get().getToken());
			if (null != branch) {
				aml.setFinc(branch.getAmlBranchCode());
				aml.setRicd(branch.getAmlBranchCode());
				aml.setJgbm(branch.getBranchCode());
			}
		}
		aml.setRpdt(new Date());
		return new Resp(aml, CodeDef.SUCCESS);
	}

	@RequestMapping(value = "/getAttasBySusId", method = RequestMethod.GET)
	public Resp getAttasBySusId(@ModelAttribute PageQuery query,
			@RequestParam(value = "id", required = false) Long id) {
		PageRequest pageable = new PageRequest(query.getPindex(), query.getPcount(), query.getSortObj());

		Specification<SusAttachment> spe = new Specification<SusAttachment>() {
			@Override
			public Predicate toPredicate(Root<SusAttachment> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				query.where(cb.equal(root.get("susId").as(Long.class), id));
				return query.getRestriction();
			}
		};
		Page<SusAttachment> page = susAttachmentRepo.findAll(Specifications.where(spe), pageable);
		return new Resp(page, CodeDef.SUCCESS);
	}

	@AuthPass
	@RequestMapping(value = "/fileUpload", method = RequestMethod.POST, produces = "text/plain;charset=utf-8 ")
	public String fileUpload(HttpServletRequest request) throws IOException {
		MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
		MultiValueMap<String, MultipartFile> fileMap = mRequest.getMultiFileMap();
		List<MultipartFile> list = fileMap.get("file");
		MultipartFile mFile = null;

		if (list != null && list.size() > 0) {
			mFile = list.get(0);
			if (mFile == null) {
				return "0";
			}
		} else {
			return "0";
		}

		String susId = mRequest.getParameter("susId");
		String username = mRequest.getParameter("username");
		String path = null;
		Long id = null;
		String fileName = mFile.getOriginalFilename();
		//兼容ie
		if(fileName.lastIndexOf("\\") != -1){
			fileName = fileName.substring(fileName.lastIndexOf("\\") + 1, fileName.length());
		}

		if (susId != null) {
			id = Long.valueOf(susId);
			List<SusAttachment> attas = susAttachmentRepo.findBySusId(id);

			if (attas != null && attas.size() > 0) {
				String filePath = attas.get(0).getFilePath();
				path = filePath.substring(0, filePath.lastIndexOf("\\") + 1);
				int i = 1;
				while (i > 0) {
					boolean flag = false;
					for (SusAttachment susAttachment : attas) {
						if (susAttachment.getFileName().equals(fileName)) {
							String fix = fileName.substring(fileName.lastIndexOf(".") + 1);
							fileName = susAttachment.getFileName().replaceFirst("\\(\\d+\\)\\.", "(" + i + ").");
							if (susAttachment.getFileName().equals(fileName)) {
								fileName = fileName.substring(0, fileName.lastIndexOf(".")) + "(" + i + ").";
								fileName += fix;
							}
							i++;
							flag = true;
							break;
						}
					}
					if (!flag) {
						break;
					}
				}
			}
		}
		OutputStream output = null;
		File newFile = null;
		if (path != null) {
			newFile = new File(path, fileName);
			path = newFile.getAbsolutePath();
		} else {
			String dirName = UUID.randomUUID().toString().replaceAll("-", "");
			File dir = new File(config.getAttachmentdir() + dirName);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			newFile = new File(dir, fileName);
			path = newFile.getAbsolutePath();
		}

		if (!newFile.exists()) {
			newFile.createNewFile();
		}
		output = new FileOutputStream(newFile);
		IOUtils.copy(mFile.getInputStream(), output);
		output.close();

		SusAttachment susAttachment = new SusAttachment();
		susAttachment.setSusId(id);
		susAttachment.setFilePath(path);
		susAttachment.setFileName(fileName);
		susAttachment.setExecutor(username);
		susAttachment.setCreateTime(new Date());

		susAttachmentRepo.save(susAttachment);
		return "1";
	}

	@AuthPass
	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public String download(Long attaId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		SusAttachment susAttachment = susAttachmentRepo.findOne(attaId);
		String path = susAttachment.getFilePath();
		File file = new File(path);

		if (!file.exists()) {
			return null;
		}

		response.setCharacterEncoding("utf-8");
		response.setContentType("multipart/form-data");
		response.setHeader("Content-Disposition",
				"attachment;fileName=" + URLEncoder.encode(susAttachment.getFileName(), "utf-8"));

		InputStream inputStream = new FileInputStream(file);
		OutputStream os = response.getOutputStream();
		IOUtils.copy(inputStream, os);

		inputStream.close();
		return null;
	}

	@RequestMapping(value = "/delFile", method = RequestMethod.GET)
	public Resp delFile(@RequestParam("id") Long id) {
		boolean flag = false;
		SusAttachment susAttachment = susAttachmentRepo.findOne(id);
		String filePath = susAttachment.getFilePath();
		File file = new File(filePath);
		if (file.exists() && file.delete()) {
			susAttachmentRepo.delete(id);
			flag = true;
		}
		return new Resp(flag, CodeDef.SUCCESS);
	}

	private void initData(AmlSuspicious suspiciou) {
		BaseTrn baseTrn = baseTrnRepo.findByJylshAndJyrqAndEnabled(suspiciou.getTicd(), suspiciou.getTstm(), "1");
		if (baseTrn != null) {
			if (suspiciou.getTbnm() == null) {
				suspiciou.setTbnm(baseTrn.getDbrmc());
			}
			if (suspiciou.getTbit() == null) {
				suspiciou.setTbit(baseTrn.getDbrzjlx());
			}
			if (suspiciou.getTbid() == null) {
				suspiciou.setTbid(baseTrn.getDbrzjhm());
			}
			if (suspiciou.getTbnt() == null) {
				suspiciou.setTbnt(baseTrn.getDbrgj());
			}
			if (suspiciou.getTrcd() == null) {
				suspiciou.setTrcd(baseTrn.getJyfsd());
			}
			if (suspiciou.getRpmt() == null) {
				suspiciou.setRpmt(baseTrn.getSfkfpphlx());
			}
			if (suspiciou.getRpmn() == null) {
				suspiciou.setRpmn(baseTrn.getSfkfpph());
			}
			if (suspiciou.getTstp() == null) {
				suspiciou.setTstp(baseTrn.getJyfs());
			}
			if (suspiciou.getOctt() == null) {
				suspiciou.setOctt(baseTrn.getFgtjyfs());
			}
			if (suspiciou.getOcec() == null) {
				suspiciou.setOcec(baseTrn.getFgtjyfssbdm());
			}
			if (suspiciou.getBptc() == null) {
				suspiciou.setBptc(baseTrn.getYhyzfjgzjdywbm());
			}
			if (suspiciou.getTsdr() == null) {
				suspiciou.setTsdr(baseTrn.getJdbj());
			}
			if (suspiciou.getCrsp() == null) {
				suspiciou.setCrsp(baseTrn.getZjyt());
			}
			if (suspiciou.getCrtp() == null) {
				suspiciou.setCrtp(baseTrn.getBz());
			}
			if (suspiciou.getCrat() == null) {
				suspiciou.setCrat(baseTrn.getJyje());
			}
			if (suspiciou.getCfct() == null) {
				suspiciou.setCfct(baseTrn.getJydswdlx());
			}
			if (suspiciou.getTcnm() == null) {
				suspiciou.setTcnm(baseTrn.getJydsmc());
			}
			if (suspiciou.getTcit() == null) {
				suspiciou.setTcit(baseTrn.getJydszjlx());
			}
			if (suspiciou.getTcid() == null) {
				suspiciou.setTcid(baseTrn.getJydszjhm());
			}
			if (suspiciou.getTcat() == null) {
				suspiciou.setTcat(baseTrn.getJydszhlx());
			}
			if (suspiciou.getTcac() == null) {
				suspiciou.setTcac(baseTrn.getJydszh());
			}
			if (baseTrn.getKhbh() != null && suspiciou.getCsnm2() == null) {
				BaseCorpCust baseCorpCust = baseCorpCustRepo.findByKhbhToOne(baseTrn.getKhbh());
				suspiciou.setCsnm2(baseTrn.getKhbh());
				if (baseCorpCust != null) {
					if (suspiciou.getCtnm() == null) {
						suspiciou.setCtnm(baseCorpCust.getKhzwmc());
					}
					if (suspiciou.getCitp() == null) {
						suspiciou.setCitp(baseCorpCust.getKhzjlx());
					}
					if (suspiciou.getCtid() == null) {
						suspiciou.setCtid(baseCorpCust.getKhzjhm());
					}
				} else {
					BaseIndvCust baseIndvCust = baseIndvCustRepo.findByKhbhToOne(baseTrn.getKhbh());
					if (baseIndvCust != null) {
						if (suspiciou.getCtnm() == null) {
							suspiciou.setCtnm(baseIndvCust.getKhzwmc());
						}
						if (suspiciou.getCitp() == null) {
							suspiciou.setCitp(baseIndvCust.getZjlx());
						}
						if (suspiciou.getCtid() == null) {
							suspiciou.setCtid(baseIndvCust.getZjhm());
						}
					}
				}
			}
			if (baseTrn.getZh() != null && suspiciou.getCtac() == null) {
				suspiciou.setCtac(baseTrn.getZh());
				BaseAif baseAif = baseAifRepo.findByZhAndEnabledAndConfirmStatus(baseTrn.getZh(), "1", "101002");
				if (baseAif != null) {
					if (suspiciou.getCatp() == null) {
						suspiciou.setCatp(baseAif.getZhlx());
					}
					if (suspiciou.getOatm() == null) {
						suspiciou.setOatm(baseAif.getKhrq());
					}
					if (suspiciou.getCatm() == null) {
						suspiciou.setCatm(baseAif.getGhrq());
					}
					if (suspiciou.getCbct() == null) {
						suspiciou.setCbct(baseAif.getYhklx());
					}
					if (suspiciou.getCbcn() == null) {
						suspiciou.setCbcn(baseAif.getYhkhm());
					}
				}
			}
		} else {
			if (suspiciou.getCsnm2() != null) {
				BaseCorpCust baseCorpCust = baseCorpCustRepo.findByKhbhToOne(suspiciou.getCsnm2());
				if (baseCorpCust != null) {
					if (suspiciou.getCtnm() == null) {
						suspiciou.setCtnm(baseCorpCust.getKhzwmc());
					}
					if (suspiciou.getCitp() == null) {
						suspiciou.setCitp(baseCorpCust.getKhzjlx());
					}
					if (suspiciou.getCtid() == null) {
						suspiciou.setCtid(baseCorpCust.getKhzjhm());
					}
				} else {
					BaseIndvCust baseIndvCust = baseIndvCustRepo.findByKhbhToOne(suspiciou.getCsnm2());
					if (baseIndvCust != null) {
						if (suspiciou.getCtnm() == null) {
							suspiciou.setCtnm(baseIndvCust.getKhzwmc());
						}
						if (suspiciou.getCitp() == null) {
							suspiciou.setCitp(baseIndvCust.getZjlx());
						}
						if (suspiciou.getCtid() == null) {
							suspiciou.setCtid(baseIndvCust.getZjhm());
						}
					}
				}
			}
			if (suspiciou.getCtac() != null) {
				BaseAif baseAif = baseAifRepo.findByZhAndEnabledAndConfirmStatus(suspiciou.getCtac(), "1", "101002");
				if (baseAif != null) {
					if (suspiciou.getCatp() == null) {
						suspiciou.setCatp(baseAif.getZhlx());
					}
					if (suspiciou.getOatm() == null) {
						suspiciou.setOatm(baseAif.getKhrq());
					}
					if (suspiciou.getCatm() == null) {
						suspiciou.setCatm(baseAif.getGhrq());
					}
					if (suspiciou.getCbct() == null) {
						suspiciou.setCbct(baseAif.getYhklx());
					}
					if (suspiciou.getCbcn() == null) {
						suspiciou.setCbcn(baseAif.getYhkhm());
					}
				}
			}
		}
		suspiciou.setReportType("N");
		suspiciou.setBwzt("xzbw");
		suspiciou.setSubmitType("Y");
		suspiciou.setTorp("1");
		suspiciou.setOrxn("@N");
		suspiciou.setOitp1("@N");
		suspiciou.setOitp2("@N");
		suspiciou.setOitp3("@N");
		suspiciou.setOitp4("@N");
		suspiciou.setOrit("@N");
		suspiciou.setOcit("@N");
		suspiciou.setOcbt("@N");
		suspiciou.setOoct("@N");
		suspiciou.setRotf1("@N");
		suspiciou.setRotf2("@N");
		suspiciou.setOdrp("@N");
		suspiciou.setOtpr("@N");
		suspiciou.setMirs("@N");
		suspiciou.setDeleteState("1");
		suspiciou.setAptp("01");
	}
}

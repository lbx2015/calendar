package net.riking.web.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.config.Config;
import net.riking.config.Const;
import net.riking.core.entity.Resp;
import net.riking.core.entity.TokenHolder;
import net.riking.core.log.InFunLog;
import net.riking.core.log.InModLog;
import net.riking.core.task.TaskMgr;
import net.riking.core.workflow.WorkflowMgr;
import net.riking.entity.PageQuery;
import net.riking.entity.model.BaseTrn;
import net.riking.entity.model.Branch;
import net.riking.service.UserBranchServiceImpl;
import net.riking.service.repo.BaseTrnRepo;
import net.sf.json.JSONObject;

@InModLog(modName = "交易信息")
@RestController
@RequestMapping(value = "/baseTrns")
public class BaseTrnController {
	@Autowired
	Config config;

	@Autowired
	TaskMgr taskMgr;

	@Autowired
	UserBranchServiceImpl userBranchServiceImpl;

	@Autowired
	BaseTrnRepo baseTrnRepo;

	@Autowired
	WorkflowMgr workflowMgr;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
	}

	// 规则: 查,删 操作接口使用RequestMethod.GET，失败情况可以重复请求
	// 增，改使用RequestMethod.POST，不能重复请求
	// 为降低难度与兼容性， DELETE,PUT等操作不用。
	@InFunLog(funName = "添加或者更新交易信息", args = { 0 })
	@ApiOperation(value = "添加或者更新交易信息", notes = "POST-@baseTrn")
	@RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
	public Resp add_(@RequestBody BaseTrn baseTrn) {
		try {
			if (null == baseTrn.getId()) {
				Long jgid = TokenHolder.get().getBranchId();
				if (null != jgid) {
					Branch branch = userBranchServiceImpl.getBranch(TokenHolder.get().getBranchId(),
							TokenHolder.get().getToken());
					if (null != branch) {
						baseTrn.setJgbm(branch.getBranchCode());
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (StringUtils.isNotEmpty(baseTrn.getJyfs())) {
			String jyfs = baseTrn.getJyfs().replaceAll(",", "");
			baseTrn.setJyfs(jyfs);
		}
		if (StringUtils.isNotEmpty(baseTrn.getJyfsd())) {
			String jyfsd = baseTrn.getJyfsd().replaceAll(",", "");
			baseTrn.setJyfsd(jyfsd);
		}
		baseTrn.setEnabled(1);
		baseTrn.setConfirmStatus("101001");
		baseTrn.setDelState("1");
		BaseTrn baseCorp = baseTrnRepo.save(baseTrn);
		baseCorp.setStartState("PRE_RECROD");
		List<BaseTrn> baseTrns = new ArrayList<>();
		baseTrns.add(baseCorp);
		workflowMgr.addJobs(config.getBaseInfoWorkId(), baseTrns);
		return new Resp(baseCorp, CodeDef.SUCCESS);
	}

	// 规则: 查,删 操作接口使用RequestMethod.GET，失败情况可以重复请求
	// 增，改使用RequestMethod.POST，不能重复请求
	// 为降低难度与兼容性， DELETE,PUT等操作不用。
	@InFunLog(funName = "审核交易信息", args = { 0 })
	@ApiOperation(value = "审核交易信息", notes = "POST-@baseTrn")
	@RequestMapping(value = "/approve", method = RequestMethod.POST)
	public Resp approve_(@RequestBody BaseTrn baseTrn) {

		baseTrn.setEnabled(0);
		baseTrn.setConfirmStatus("101002");
		BaseTrn baseCorp = baseTrnRepo.save(baseTrn);
		baseCorp.setStartState("PRE_RECROD");
		workflowMgr.addJobs(config.getBaseInfoWorkId(), Arrays.asList(baseCorp));
		return new Resp(baseCorp, CodeDef.SUCCESS);
	}

	// 规则: 查,删 操作接口使用RequestMethod.GET，失败情况可以重复请求
	// 增，改使用RequestMethod.POST，不能重复请求
	// 为降低难度与兼容性， DELETE,PUT等操作不用。
	@InFunLog(funName = "变更交易信息", args = { 0 })
	@ApiOperation(value = "变更交易信息", notes = "POST-@baseTrn")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Resp update_(@RequestBody BaseTrn baseTrn) {
		BaseTrn baseTrnCopy = null;
		try {
			JSONObject json = JSONObject.fromObject(baseTrn);
			baseTrnCopy = (BaseTrn) JSONObject.toBean(json, BaseTrn.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		baseTrnCopy.setEnabled(0);
		if (StringUtils.isNotEmpty(baseTrn.getJyfs())) {
			String jyfs = baseTrn.getJyfs().replaceAll(",", "");
			baseTrn.setJyfs(jyfs);
		}if (StringUtils.isNotEmpty(baseTrn.getJyfsd())) {
			String jyfsd = baseTrn.getJyfsd().replaceAll(",", "");
			baseTrn.setJyfsd(jyfsd);
		}
		baseTrn.setId(null);
		baseTrn.setJob(null);
		baseTrn.setJobId(null);
		baseTrn.setFlowOwner(null);
		baseTrn.setFlowState(null);
		baseTrn.setEnabled(1);
		baseTrn.setConfirmStatus("101001");
		BaseTrn baseCorp = baseTrnRepo.save(baseTrn);
		if (baseCorp==null) {
			return new Resp(2, CodeDef.SUCCESS);	
		} 
		baseCorp.setStartState("PRE_RECROD");
		workflowMgr.addJobs(config.getBaseInfoWorkId(), Arrays.asList(baseCorp));
		return new Resp(1, CodeDef.SUCCESS);
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public Resp get_(@RequestParam("id") Long id) {
		BaseTrn baseTrn = baseTrnRepo.findOne(id);
		String jyfs = baseTrn.getJyfs();
		String jyfsd = baseTrn.getJyfsd();
		if (StringUtils.isNotEmpty(jyfs) && jyfs.length() >= 6) {
			if (jyfs.substring(0, 2).equals("00")) {
				baseTrn.setJyfs(jyfs.substring(0, 2) + "," + jyfs.substring(2, 4) + "," + jyfs.substring(4));
			} else {
				baseTrn.setJyfs(jyfs.substring(0, 2) + "," + jyfs.substring(2));
			}
		}
		
		if (StringUtils.isNotEmpty(jyfsd) && jyfsd.length() > 3) {
			String jyfsds = jyfsd.substring(0, 3) + "," + jyfsd.substring(3);
			baseTrn.setJyfsd(jyfsds);
		}
		workflowMgr.fillJobsInfo(Arrays.asList(baseTrn));
		return new Resp(baseTrn, CodeDef.SUCCESS);
	}

	@InFunLog(funName = "删除", args = { 0 }, argNames = { "ID" })
	@RequestMapping(value = "/delMore", method = RequestMethod.POST)
	public Resp delMore(@RequestBody Set<Long> ids) {
		Integer count =ids.size()- baseTrnRepo.delById(ids);
		return new Resp(count, CodeDef.SUCCESS);
	}

	@RequestMapping(value = "/getMore", method = RequestMethod.GET)
	public Resp getMore(@ModelAttribute PageQuery query, @ModelAttribute BaseTrn bc) {
		PageRequest pageable = new PageRequest(query.getPindex(), query.getPcount(), query.getSortObj());
		/*
		 * Specification<BaseTrn> s1 = new Specification<BaseTrn>() {
		 * 
		 * @Override public Predicate toPredicate(Root<BaseTrn> root,
		 * CriteriaQuery<?> query, CriteriaBuilder cb) { List<Predicate> list =
		 * new ArrayList<Predicate>();
		 * list.add(cb.equal((root.get("enabled").as(Integer.class)),0));
		 * list.add(cb.equal((root.get("confirmStatus").as(String.class)),
		 * "101002")); Predicate[] p = new Predicate[list.size()]; return
		 * cb.not(cb.and(list.toArray(p))); } };
		 */
		Specification<BaseTrn> s2 = new Specification<BaseTrn>() {
			@Override
			public Predicate toPredicate(Root<BaseTrn> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();

				if (null != bc.getZh()) {
					list.add(cb.like((root.get("zh").as(String.class)), "%" + bc.getZh() + "%"));
				}
				if (null != bc.getYwbh()) {
					list.add(cb.like((root.get("ywbh").as(String.class)), "%" + bc.getYwbh() + "%"));
				}
				if (bc.getStarts() != null || bc.getEnds() != null) {
					if (bc.getEnds() == null) {
						list.add(cb.greaterThanOrEqualTo(root.<Date> get("jyrq"), bc.getStarts()));
					} else if (bc.getStarts() == null) {
						Long time = bc.getEnds().getTime();
						time = time + 24 * 60 * 60 * 1000;
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						Date date = new Date(time);
						try {
							bc.setEnds(sdf.parse(sdf.format(date)));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						list.add(cb.lessThan(root.<Date> get("jyrq"), bc.getEnds()));
					} else {
						Long time = bc.getEnds().getTime();
						time = time + 24 * 60 * 60 * 1000 - 1;
						DateFormat dfs = new SimpleDateFormat("yyyyMMddHHmmssSSS");
						Date date = new Date(time);
						try {
							bc.setEnds(dfs.parse(dfs.format(date)));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						list.add(cb.between(root.<Date> get("jyrq"), bc.getStarts(), bc.getEnds()));
					}
				}
				List<String> confirmStatus = new ArrayList<>();
				confirmStatus.add("101001");
				confirmStatus.add("101002");
				list.add(root.get("confirmStatus").as(String.class).in(confirmStatus));
				list.add(root.get("jobId").as(String.class).isNotNull());
				// 非超级管理员
				if (null != TokenHolder.get().getBranchId()) {
					List<String> branchCodes = userBranchServiceImpl.get(TokenHolder.get().getUserId());
					if (branchCodes != null && branchCodes.size() > 0) {
						list.add(root.get("jgbm").as(String.class).in(branchCodes));
					}
				}
				list.add(cb.equal((root.get("delState").as(String.class)), "1"));
				Predicate[] p = new Predicate[list.size()];
				return cb.and(list.toArray(p));
			}
		};
		Page<BaseTrn> page = baseTrnRepo.findAll(Specifications.where(s2), pageable);
		workflowMgr.fillJobsInfo(page.getContent());
		return new Resp(page);
	}

	@RequestMapping(value = "/getOther", method = RequestMethod.GET)
	public Resp getOther(@RequestParam("id") Long id) {
		BaseTrn baseTrn = baseTrnRepo.findOne(id);
		List<BaseTrn> other = baseTrnRepo.findByJylshAndNotId(baseTrn.getJylsh(), baseTrn.getId());
		if (other.size() > 0) {
			return new Resp(false, CodeDef.SUCCESS);
		}
		return new Resp(true, CodeDef.SUCCESS);
	}

	@RequestMapping(value = "/getLast", method = RequestMethod.GET)
	public Resp getLast(@RequestParam String batchId) {
		// 获取最新的采集结果
		if (StringUtils.isNotEmpty(batchId)) {
			return new Resp(taskMgr.getJobInfo(batchId), CodeDef.SUCCESS);
		} else {
			return new Resp(taskMgr.getLastJobInfoByGroup(Const.TASK_CUST_GRADE_GROUP), CodeDef.SUCCESS);
		}
	}
}

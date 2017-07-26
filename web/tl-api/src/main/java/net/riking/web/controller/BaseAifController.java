package net.riking.web.controller;

import java.lang.reflect.InvocationTargetException;
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

import org.apache.commons.beanutils.BeanUtils;
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
import net.riking.entity.model.BaseAif;
import net.riking.entity.model.Branch;
import net.riking.service.UserBranchServiceImpl;
import net.riking.service.repo.BaseAifRepo;

@InModLog(modName="账户信息")
@RestController
@RequestMapping(value = "/baseAifs")
public class BaseAifController {
	@Autowired
	Config config;
	@Autowired
	TaskMgr taskMgr;
	@Autowired
	BaseAifRepo baseAifRepo;
	@Autowired
	WorkflowMgr workflowMgr;
	@Autowired
	UserBranchServiceImpl userBranchServiceImpl;
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
	}

	// 规则: 查,删 操作接口使用RequestMethod.GET，失败情况可以重复请求
	// 增，改使用RequestMethod.POST，不能重复请求
	// 为降低难度与兼容性， DELETE,PUT等操作不用。
	@InFunLog(funName = "添加或者更新账户信息", args = { 0 })
	@ApiOperation(value = "添加或者更新账户信息", notes = "POST-@baseAif")
	@RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
	public Resp add_(@RequestBody BaseAif baseAif) {
		try {
			if (null == baseAif.getId()) {
				Long jgid = TokenHolder.get().getBranchId();
				if (null != jgid) {
					Branch branch = userBranchServiceImpl.getBranch(TokenHolder.get().getBranchId(),
							TokenHolder.get().getToken());
					if (null != branch) {
						baseAif.setJgbm(branch.getBranchCode());
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		baseAif.setEnabled("1");
		baseAif.setConfirmStatus("101001");
		BaseAif baseCorp = baseAifRepo.save(baseAif);

		baseCorp.setStartState("PRE_RECROD");
		workflowMgr.addJobs(config.getBaseInfoWorkId(), Arrays.asList(baseCorp));
		return new Resp(baseCorp, CodeDef.SUCCESS);
	}

	/*	// 规则: 查,删 操作接口使用RequestMethod.GET，失败情况可以重复请求
		// 增，改使用RequestMethod.POST，不能重复请求
		// 为降低难度与兼容性， DELETE,PUT等操作不用。
		@InFunLog(funName = "审核账户信息", args = { 0 })
		@ApiOperation(value = "审核账户信息", notes = "POST-@baseAif")
		@RequestMapping(value = "/approve", method = RequestMethod.POST)
		public Resp approve_(@RequestBody BaseAif baseAif) {

			baseAif.setEnabled("1");
			baseAif.setConfirmStatus("101002");
			BaseAif baseCorp = baseAifRepo.save(baseAif);

			baseCorp.setStartState("PRE_RECROD");
			workflowMgr.addJobs(config.getBaseInfoWorkId(), Arrays.asList(baseCorp));
			return new Resp(baseCorp, CodeDef.SUCCESS);
		}
	 */

	// 规则: 查,删 操作接口使用RequestMethod.GET，失败情况可以重复请求
	// 增，改使用RequestMethod.POST，不能重复请求
	// 为降低难度与兼容性， DELETE,PUT等操作不用。
	@InFunLog(funName = "变更账户信息", args = { 0 })
	@ApiOperation(value = "变更账户信息", notes = "POST-@baseAif")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Resp update_(@RequestBody BaseAif baseAif) {

		BaseAif baseAifCopy=null;
		try {
			baseAifCopy = (BaseAif)BeanUtils.cloneBean(baseAif);
		} catch (IllegalAccessException | InstantiationException | InvocationTargetException
				| NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		baseAif.setId(null);
		baseAif.setEnabled("1");
		baseAif.setConfirmStatus("101001");
		baseAif.setJob(null);
		baseAif.setJobId(null);
		baseAif.setFlowOwner(null);
		baseAif.setFlowState(null);
		baseAif.setOldId(baseAifCopy.getId());
		BaseAif baseCorp = baseAifRepo.save(baseAif);
		if (baseCorp==null) {
			return new Resp(2, CodeDef.ERROR);
		}
		baseCorp.setStartState("PRE_RECROD");
		workflowMgr.addJobs(config.getBaseInfoWorkId(), Arrays.asList(baseCorp));
		return new Resp(1, CodeDef.SUCCESS);
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public Resp get_(@RequestParam("id") Long id) {
		BaseAif baseAif = baseAifRepo.findOne(id);
		workflowMgr.fillJobsInfo(Arrays.asList(baseAif));
		return new Resp(baseAif, CodeDef.SUCCESS);
	}


	@InFunLog(funName = "删除", args = { 0 }, argNames = { "ID" })
	@RequestMapping(value = "/delMore", method = RequestMethod.POST)
	public Resp delMore(@RequestBody Set<Long> ids) {
		Integer sum = ids.size()-baseAifRepo.delById(ids);
		return new Resp(sum);
	}


	@RequestMapping(value = "/getMore", method = RequestMethod.GET)
	public Resp getMore(@ModelAttribute PageQuery query, @ModelAttribute BaseAif bc) {
		PageRequest pageable = new PageRequest(query.getPindex(), query.getPcount(), query.getSortObj());
		Specification<BaseAif> s1 = new Specification<BaseAif>() {
			@Override
			public Predicate toPredicate(Root<BaseAif> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				list.add(cb.equal((root.get("enabled").as(String.class)),"1"));
				list.add(root.get("jobId").as(String.class).isNotNull());
				Predicate[] p = new Predicate[list.size()];
				return cb.and(list.toArray(p));
			}
		};
		Specification<BaseAif> s2 = new Specification<BaseAif>() {
			@Override
			public Predicate toPredicate(Root<BaseAif> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();

				if (null != bc.getKhbh()) {
					list.add(cb.like((root.get("khbh").as(String.class)),"%"+bc.getKhbh()+"%"));
				}
				if (null != bc.getZh()) {
					list.add(cb.like((root.get("zh").as(String.class)),"%"+bc.getZh()+"%"));
				}
				List<String> confirmStatus = new ArrayList<>();
				confirmStatus.add("101001");
				confirmStatus.add("101002");
				list.add(root.get("confirmStatus").as(String.class).in(confirmStatus));
				// 非超级管理员
				if (null != TokenHolder.get().getBranchId()) {
					List<String> branchCodes = userBranchServiceImpl.get(TokenHolder.get().getUserId());
					if (branchCodes != null && branchCodes.size() > 0) {
						list.add(root.get("jgbm").as(String.class).in(branchCodes));
					}
				}
				if (bc.getStarts() != null || bc.getEnds() != null) {
					if (bc.getEnds() == null) {
						list.add(cb.greaterThanOrEqualTo(root.<Date> get("sjrq"), bc.getStarts()));
					} else if (bc.getStarts() == null) {
						Long time = bc.getEnds().getTime();
						time = time + 24*60*60*1000; 
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						Date date = new Date(time);
						try {
							bc.setEnds(sdf.parse(sdf.format(date)));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						list.add(cb.lessThan(root.<Date> get("sjrq"), bc.getEnds()));
					} else {
						Long time = bc.getEnds().getTime();
						time = time + 24*60*60*1000-1; 
						DateFormat dfs = new SimpleDateFormat("yyyyMMddHHmmssSSS");
						Date date = new Date(time);
						try {
							bc.setEnds(dfs.parse(dfs.format(date)));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						list.add(cb.between(root.<Date> get("sjrq"), bc.getStarts(), bc.getEnds()));
					}
				}
				Predicate[] p = new Predicate[list.size()];
				return cb.and(list.toArray(p));
			}
		};
		Page<BaseAif> page = baseAifRepo.findAll(Specifications.where(s2).and(s1), pageable);
		workflowMgr.fillJobsInfo(page.getContent());
		return new Resp(page);
	}

	@RequestMapping(value = "/getOther", method = RequestMethod.GET)
	public Resp getOther(@RequestParam("id")Long id) {
		BaseAif baseAif = baseAifRepo.findOne(id);
		List<BaseAif> other = baseAifRepo.findByKhbhAndNotId(baseAif.getKhbh(),baseAif.getId());
		if(other.size()>0){
			return new Resp(false , CodeDef.SUCCESS);
		}
		return new Resp(true , CodeDef.SUCCESS);
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
	@RequestMapping(value = "/findOne", method = RequestMethod.GET)
	public Resp findOne(@RequestParam("ctac") String ctac) {
		BaseAif aif = baseAifRepo.findByZhAndEnabledAndConfirmStatus(ctac, "1", "101002");
		if(aif==null){
			aif = new BaseAif();
			aif.setKhrq(new Date(0L));
			aif.setYhklx("--##--");
			aif.setYhkhm("");
		}
		aif.setKhbh(null);
		aif.setJob(null);
		return new Resp(aif , CodeDef.SUCCESS);
	}
}

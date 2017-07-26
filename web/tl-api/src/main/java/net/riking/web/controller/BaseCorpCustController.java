package net.riking.web.controller;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
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

import net.riking.core.entity.TokenHolder;
import net.riking.core.log.InFunLog;
import net.riking.core.log.InModLog;
import net.riking.entity.model.Branch;
import net.riking.service.UserBranchServiceImpl;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.config.Config;
import net.riking.config.Const;
import net.riking.core.entity.Resp;
import net.riking.core.task.TaskMgr;
import net.riking.core.workflow.WorkflowMgr;
import net.riking.entity.PageQuery;
import net.riking.entity.model.BaseCorpCust;
import net.riking.service.RisKRateServiceImpl;
import net.riking.service.repo.BaseCorpCustRepo;
import net.riking.task.job.BaseCorpCustJob;
import net.riking.util.CheckUtil;

@InModLog(modName = "对公客户管理")
@RestController
@RequestMapping(value = "/baseCorpCusts")
public class BaseCorpCustController {
	@Autowired
	Config config;

	@Autowired
	TaskMgr taskMgr;

	@Autowired
	BaseCorpCustJob baseCorpCustJob;

	@Autowired
	BaseCorpCustRepo baseCorpCustRepo;

	@Autowired
	RisKRateServiceImpl risKRateServiceImpl;

	@Autowired
	private UserBranchServiceImpl userBranchServiceImpl;

	@Autowired
	WorkflowMgr workflowMgr;

	@Autowired
	CheckUtil check;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
	}

	// 规则: 查,删 操作接口使用RequestMethod.GET，失败情况可以重复请求
	// 增，改使用RequestMethod.POST，不能重复请求
	// 为降低难度与兼容性， DELETE,PUT等操作不用。
	@InFunLog(funName = "添加或者更新", args = { 0 })
	@ApiOperation(value = "添加或者更新对公客户信息", notes = "POST-@BaseCorpCust")
	@RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
	public Resp add_(@RequestBody BaseCorpCust baseCorpCust) throws Exception {
		if (null == baseCorpCust.getId()) {
			Long jgid = TokenHolder.get().getBranchId();
			if (null != jgid) {
				Branch branch = userBranchServiceImpl.getBranch(TokenHolder.get().getBranchId(),
						TokenHolder.get().getToken());
				if (null != branch) {
					baseCorpCust.setJgbm(branch.getBranchCode());
				}
			}
		}
		baseCorpCust.setEnabled("1");
		baseCorpCust.setConfirmStatus("101001");
		BaseCorpCust baseCorp = baseCorpCustRepo.save(baseCorpCust);
		baseCorp.setStartState("PRE_RECROD");
		workflowMgr.addJobs(config.getBaseInfoWorkId(), Arrays.asList(baseCorp));
		return new Resp(baseCorp, CodeDef.SUCCESS);
	}

	@InFunLog(funName = "变更", args = { 0 })
	@ApiOperation(value = "变更对公客户信息", notes = "POST-@BaseCorpCust")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Resp update_(@RequestBody BaseCorpCust baseCorpCust) {
		BaseCorpCust baseCorpCustCopy = null;
		try {
			baseCorpCustCopy = (BaseCorpCust) BeanUtils.cloneBean(baseCorpCust);
		} catch (IllegalAccessException | InstantiationException | InvocationTargetException
				| NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		baseCorpCust.setId(null);
		baseCorpCust.setEnabled("1");
		baseCorpCust.setConfirmStatus("101001");
		baseCorpCust.setOldId(baseCorpCustCopy.getId());
		baseCorpCust.setJob(null);
		baseCorpCust.setJobId(null);
		baseCorpCust.setFlowOwner(null);
		baseCorpCust.setFlowState(null);
		BaseCorpCust baseCorp = baseCorpCustRepo.save(baseCorpCust);
		if (baseCorp==null) {
			return new Resp(2, CodeDef.SUCCESS);	
		} 
		baseCorp.setStartState("PRE_RECROD");
		workflowMgr.addJobs(config.getBaseInfoWorkId(), Arrays.asList(baseCorp));
		return new Resp(1, CodeDef.SUCCESS);
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public Resp get_(@RequestParam("id") Long id) {
		BaseCorpCust baseCorpCust = baseCorpCustRepo.findOne(id);
		workflowMgr.fillJobsInfo(Arrays.asList(baseCorpCust));
		return new Resp(baseCorpCust, CodeDef.SUCCESS);
	}

	@InFunLog(funName = "删除", args = { 0 }, argNames = { "ID" })
	@RequestMapping(value = "/delMore", method = RequestMethod.POST)
	public Resp delMore(@RequestBody Set<Long> ids) {
		Integer count =ids.size()- baseCorpCustRepo.delById(ids);
		return new Resp(count);
	}

	@RequestMapping(value = "/getMore", method = RequestMethod.GET)
	public Resp getMore(@ModelAttribute PageQuery query, @ModelAttribute BaseCorpCust bc)  {
		PageRequest pageable = new PageRequest(query.getPindex(), query.getPcount(), query.getSortObj());
		Specification<BaseCorpCust> s1 = new Specification<BaseCorpCust>() {
			@Override
			public Predicate toPredicate(Root<BaseCorpCust> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				list.add(cb.equal((root.get("enabled").as(String.class)), "1"));
				// 客户编号
				list.add(root.get("jobId").as(String.class).isNotNull());
				if (StringUtils.isNotEmpty(bc.getKhbh())) {
					  String  Khbh = bc.getKhbh();
						try {
							Khbh = java.net.URLDecoder.decode(Khbh, "UTF-8");
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					list.add(cb.like((root.get("khbh").as(String.class)), "%" + Khbh + "%"));
				}
				// 客户风险等级
				if (StringUtils.isNotEmpty(bc.getKhfxdj())) {
					String Khfxdj =bc.getKhfxdj();
					try {
						Khfxdj = java.net.URLDecoder.decode(Khfxdj, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					list.add(cb.equal((root.get("khfxdj").as(String.class)),Khfxdj));
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
				Predicate[] p = new Predicate[list.size()];
				return cb.and(list.toArray(p));
			}
		};
		Specification<BaseCorpCust> s2 = null;
		if (StringUtils.isNotEmpty(bc.getKhzwmc())) {
			s2 = new Specification<BaseCorpCust>() {
				@Override
				public Predicate toPredicate(Root<BaseCorpCust> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					List<Predicate> list = new ArrayList<Predicate>();
					String Khzwmc =bc.getKhzwmc();
					try {
						Khzwmc = java.net.URLDecoder.decode(Khzwmc, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// 客户中文名称
					list.add(cb.like((root.get("khzwmc").as(String.class)), "%" + Khzwmc + "%"));
					// 客户英文名称
					list.add(cb.like((root.get("khywmc").as(String.class)), "%" + Khzwmc + "%"));
					Predicate[] p = new Predicate[list.size()];
					return cb.or(list.toArray(p));
				}
			};
		}
		Specification<BaseCorpCust> s3 = null;
		if (StringUtils.isNotEmpty(bc.getKhzjhm())) {
			s3 = new Specification<BaseCorpCust>() {
				@Override
				public Predicate toPredicate(Root<BaseCorpCust> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					List<Predicate> list = new ArrayList<Predicate>();
					String Khzjhm =bc.getKhzjhm();
					try {
						Khzjhm = java.net.URLDecoder.decode(Khzjhm, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// 社会统一编码
					list.add(cb.like((root.get("khzjhm").as(String.class)), "%" + Khzjhm + "%"));
					// 组织机构代码
					list.add(cb.like((root.get("jrjgdm").as(String.class)), "%" +Khzjhm + "%"));
					Predicate[] p = new Predicate[list.size()];
					return cb.or(list.toArray(p));
				}
			};
		}
		Page<BaseCorpCust> page = baseCorpCustRepo.findAll(Specifications.where(s2).and(s1).and(s3), pageable);
		workflowMgr.fillJobsInfo(page.getContent());
		return new Resp(page);
	}

	@InFunLog(funName = "评级", args = { 0 })
	@RequestMapping(value = "/riskRate", method = RequestMethod.POST)
	public Resp riskRate(@RequestBody Set<Long> ids) throws JsonProcessingException, InstantiationException,
			IllegalAccessException, InvocationTargetException, NoSuchMethodException, InterruptedException {
		List<BaseCorpCust> customers = baseCorpCustRepo.findByides(ids);
		int count =  risKRateServiceImpl.baseCorpCustRiskRate(customers);
		 risKRateServiceImpl.setRateTime(customers).get_data();
		return new Resp(count);
	}

	@InFunLog(funName = "全部评级", args = { 0 })
	@RequestMapping(value = "/riskRateAll", method = RequestMethod.POST)
	public Resp riskRateAll() throws JsonProcessingException, InstantiationException, IllegalAccessException {
		String batchId = null;
		batchId = taskMgr.addJob(Const.TASK_CUST_GRADE_GROUP, "", baseCorpCustJob);
		return new Resp(taskMgr.getJobInfo(batchId), CodeDef.SUCCESS);
	}

	@RequestMapping(value = "/getOther", method = RequestMethod.GET)
	public Resp getOther(@RequestParam("id") Long id) {
		BaseCorpCust baseCorpCust = baseCorpCustRepo.findOne(id);
		List<BaseCorpCust> other = baseCorpCustRepo.findByKhbhAndNotId(baseCorpCust.getKhbh(), baseCorpCust.getId());
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

	// 可以大额edit 自动补全
	@RequestMapping(value = "/findOne", method = RequestMethod.GET)
	public Resp findOne(@RequestParam("id") Long id , @RequestParam("ctid") String ctid) {
		BaseCorpCust corpCust = baseCorpCustRepo.findByIdAndKhzjhm(id,ctid);
		if(corpCust!=null){
			if(corpCust.getKhzjlx().equals("619999")||corpCust.getKhzjlx().equals("629999")){
				corpCust.setZjlxsm("特殊机构赋码");
			}else{
				corpCust.setZjlxsm("@N");
			}
			if(StringUtils.isNotEmpty(corpCust.getKhzwmc())){
				corpCust.setKhywmc(corpCust.getKhzwmc());
			}
			corpCust.setJob(null);
			return new Resp(corpCust, CodeDef.SUCCESS);
		}
		return new Resp(new BaseCorpCust(), CodeDef.SUCCESS);
	}
}

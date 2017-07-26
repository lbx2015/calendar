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
import net.riking.core.task.TaskMgr;
import net.riking.entity.model.Branch;
import net.riking.service.RisKInRateServiceImpl;
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
import net.riking.core.workflow.WorkflowMgr;
import net.riking.entity.PageQuery;
import net.riking.entity.model.BaseIndvCust;
import net.riking.service.repo.BaseIndvCustAddRepo;
import net.riking.service.repo.BaseIndvCustRepo;
import net.riking.task.job.BaseIndvCustJob;

@InModLog(modName = "对私客户信息")
@RestController
@RequestMapping(value = "/baseIndvCusts")
public class BaseIndvCustController {
	@Autowired
	Config config;

	@Autowired
	TaskMgr taskMgr;

	@Autowired
	BaseIndvCustJob baseIndvCustJob;

	@Autowired
	BaseIndvCustRepo baseIndvCustRepo;

	@Autowired
	BaseIndvCustAddRepo baseIndvCustAddRepo;

	@Autowired
	RisKInRateServiceImpl risKInRateServiceImpl;

	@Autowired
	private UserBranchServiceImpl userBranchServiceImpl;

	@Autowired
	WorkflowMgr workflowMgr;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
	}

	// 规则: 查,删 操作接口使用RequestMethod.GET，失败情况可以重复请求
	// 增，改使用RequestMethod.POST，不能重复请求
	// 为降低难度与兼容性， DELETE,PUT等操作不用。
	@InFunLog(funName = "添加或者更新对私客户信息", args = { 0 })
	@ApiOperation(value = "添加或者更新对私客户信息", notes = "POST-@BaseIndvCust")
	@RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
	public Resp add_(@RequestBody BaseIndvCust baseIndvCust) throws Exception {
		if (null == baseIndvCust.getId()) {
			Long jgid = TokenHolder.get().getBranchId();
			if (null != jgid) {
				Branch branch = userBranchServiceImpl.getBranch(TokenHolder.get().getBranchId(),
						TokenHolder.get().getToken());
				if (null != branch) {
					baseIndvCust.setJgbm(branch.getBranchCode());
				}
			}
		}
		baseIndvCust.getBaseIndvCustAdd().setDrrq(baseIndvCust.getDrrq());
		baseIndvCustAddRepo.save(baseIndvCust.getBaseIndvCustAdd());
		baseIndvCust.setEnabled("1");
		baseIndvCust.setConfirmStatus("101001");
		BaseIndvCust baseIndv = baseIndvCustRepo.save(baseIndvCust);
		baseIndvCust.setStartState("PRE_RECROD");
		workflowMgr.addJobs(config.getBaseInfoWorkId(), Arrays.asList(baseIndvCust));
		return new Resp(baseIndv, CodeDef.SUCCESS);
	}

	@InFunLog(funName = "变更对私客户信息", args = { 0 })
	@ApiOperation(value = "变更对私客户信息", notes = "POST-@BaseIndvCust")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Resp update_(@RequestBody BaseIndvCust baseIndvCust) {

		BaseIndvCust baseIndvCustCopy = null;
		try {
			baseIndvCustCopy = (BaseIndvCust) BeanUtils.cloneBean(baseIndvCust);
		} catch (IllegalAccessException | InstantiationException | InvocationTargetException
				| NoSuchMethodException e) {
			e.printStackTrace();
		}
		baseIndvCust.getBaseIndvCustAdd().setId(null);
		baseIndvCustAddRepo.save(baseIndvCust.getBaseIndvCustAdd());
		baseIndvCust.setId(null);
		baseIndvCust.setJobId(null);
		baseIndvCust.setFlowOwner(null);
		baseIndvCust.setFlowState(null);
		baseIndvCust.setEnabled("1");
		baseIndvCust.setConfirmStatus("101001");
		baseIndvCust.setOldId(baseIndvCustCopy.getId());
		baseIndvCust.setJob(null);
		BaseIndvCust baseIndv = baseIndvCustRepo.save(baseIndvCust);
		if (baseIndv==null) {
			return new Resp(2, CodeDef.SUCCESS);	
		} 
		baseIndv.setStartState("PRE_RECROD");
		workflowMgr.addJobs(config.getBaseInfoWorkId(), Arrays.asList(baseIndv));
		return new Resp(1, CodeDef.SUCCESS);
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public Resp get_(@RequestParam("id") Long id) {
		BaseIndvCust baseIndvCust = baseIndvCustRepo.findOne(id);
		workflowMgr.fillJobsInfo(Arrays.asList(baseIndvCust));
		return new Resp(baseIndvCust, CodeDef.SUCCESS);
	}

	@InFunLog(funName = "删除", args = { 0 }, argNames = { "ID" })
	@RequestMapping(value = "/delMore", method = RequestMethod.POST)
	public Resp delMore(@RequestBody Set<Long> ids) {
		Integer count =ids.size()- baseIndvCustRepo.delById(ids);
		return new Resp(count);
	}

	@RequestMapping(value = "/getOther", method = RequestMethod.GET)
	public Resp getOther(@RequestParam("id") Long id) {
		BaseIndvCust baseIndvCust = baseIndvCustRepo.findOne(id);
		List<BaseIndvCust> other = baseIndvCustRepo.findByKhbhAndNotId(baseIndvCust.getKhbh(), baseIndvCust.getId());
		if (other.size() > 0) {
			return new Resp(false, CodeDef.SUCCESS);
		}
		return new Resp(true, CodeDef.SUCCESS);
	}

	@RequestMapping(value = "/getMore", method = RequestMethod.GET)
	public Resp getMore(@ModelAttribute PageQuery query, @ModelAttribute BaseIndvCust bc) {
		PageRequest pageable = new PageRequest(query.getPindex(), query.getPcount(), query.getSortObj());
		Specification<BaseIndvCust> s1 = new Specification<BaseIndvCust>() {
			@Override
			public Predicate toPredicate(Root<BaseIndvCust> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				list.add(cb.equal((root.get("enabled").as(String.class)), "1"));
				if (null != bc.getKhbh()) {
					String Khbh = bc.getKhbh();
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
					String Khfxdj = bc.getKhfxdj();
					try {
						Khfxdj = java.net.URLDecoder.decode(Khfxdj, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					list.add(cb.equal((root.get("khfxdj").as(String.class)), Khfxdj));
				}
				if (null != bc.getZjhm()) {
					String Zjhm = bc.getZjhm();
					try {
						Zjhm = java.net.URLDecoder.decode(Zjhm, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					list.add(cb.like((root.get("zjhm").as(String.class)), "%" + Zjhm + "%"));
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
				Predicate[] p = new Predicate[list.size()];
				return cb.and(list.toArray(p));
			}
		};
		Specification<BaseIndvCust> s2 = null;
		if (StringUtils.isNotEmpty(bc.getKhzwmc())) {
			s2 = new Specification<BaseIndvCust>() {
				@Override
				public Predicate toPredicate(Root<BaseIndvCust> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					List<Predicate> list = new ArrayList<Predicate>();
					String Khzwmc = bc.getKhzwmc();
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
		Page<BaseIndvCust> page = baseIndvCustRepo.findAll(Specifications.where(s2).and(s1), pageable);
		workflowMgr.fillJobsInfo(page.getContent());
		return new Resp(page);
	}

	@RequestMapping(value = "/riskRate", method = RequestMethod.POST)
	public Resp riskRate(@RequestBody Set<Long> ids) throws JsonProcessingException, InstantiationException,
			IllegalAccessException, InvocationTargetException, NoSuchMethodException, InterruptedException {
		List<BaseIndvCust> customers = baseIndvCustRepo.findByides(ids);
		int count = risKInRateServiceImpl.baseIndvCustRiskRate(customers);
		risKInRateServiceImpl.setRateTime(customers).get_data();
		return new Resp(count);
	}

	@RequestMapping(value = "/riskRateAll", method = RequestMethod.POST)
	public Resp riskRateAll() throws JsonProcessingException, InstantiationException, IllegalAccessException {
		String batchId = null;
		batchId = taskMgr.addJob(Const.TASK_CUST_GRADE_GROUP, "", baseIndvCustJob);
		return new Resp(taskMgr.getJobInfo(batchId), CodeDef.SUCCESS);
	}

	@RequestMapping(value = "/findOne", method = RequestMethod.GET)
	public Resp findOne(@RequestParam("id") Long id ,@RequestParam("ctid") String ctid) {
		BaseIndvCust indvCust = baseIndvCustRepo.findByIdAndZjhm(id,ctid);
		if(indvCust!=null){
			if(indvCust.getZjlx().equals("619999")||indvCust.getZjlx().equals("629999")){
				indvCust.setZjlxsm("户口簿");
			}else{
				indvCust.setZjlxsm("@N");
			}
			if(StringUtils.isNotEmpty(indvCust.getKhzwmc())){
				indvCust.setKhywmc(indvCust.getKhzwmc());
			}
			if(indvCust.getBaseIndvCustAdd()!=null){
				indvCust.setZy(indvCust.getBaseIndvCustAdd().getZy());
				if(StringUtils.isNotEmpty(indvCust.getBaseIndvCustAdd().getYddh())){
					indvCust.setYddhOrZzdh(indvCust.getBaseIndvCustAdd().getYddh());
				}else{
					indvCust.setYddhOrZzdh(indvCust.getBaseIndvCustAdd().getZzdh());
				}
			}
			indvCust.setJob(null);
			return new Resp(indvCust, CodeDef.SUCCESS);
		}
		indvCust = new BaseIndvCust();
		indvCust.setKhbh("");
		indvCust.setKhzwmc("");
		indvCust.setZjlx("--##--");
		indvCust.setZjlxsm("@N");
		indvCust.setKhgj("");
		indvCust.setLxfs("");
		indvCust.setYddhOrZzdh("");
		indvCust.setTxdz("");
		indvCust.setZy("");
		return new Resp(indvCust, CodeDef.SUCCESS);
	}
}

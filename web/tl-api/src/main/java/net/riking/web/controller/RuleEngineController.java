package net.riking.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.config.Config;
import net.riking.core.entity.Resp;
import net.riking.core.entity.model.ModelPropDict;
import net.riking.core.log.InFunLog;
import net.riking.core.log.InModLog;
import net.riking.core.service.DataDictService;
import net.riking.entity.PageQuery;
import net.riking.entity.model.AmlRuleEngine;
import net.riking.entity.model.AmlRuleEngineScore;
import net.riking.entity.model.SuspiciousPropDict;
import net.riking.service.repo.AmlRuleEngineRepo;
import net.riking.service.repo.AmlRuleEngineScoreRepo;
import net.riking.service.repo.SuspiciousPropDictRepo;

@InModLog(modName = "规则引擎")
@RestController
@RequestMapping(value = "/amlRuleEngine")
public class RuleEngineController {
	@Autowired
	Config config;

	@Autowired
	AmlRuleEngineRepo amlRuleEngineRepo;

	@Autowired
	SuspiciousPropDictRepo suspiciousPropDictRepo;

	@Autowired
	DataDictService dataDictService;

	@Autowired
	AmlRuleEngineScoreRepo amlRuleEngineScoreRepo;

	// 规则: 查,删 操作接口使用RequestMethod.GET，失败情况可以重复请求
	// 增，改使用RequestMethod.POST，不能重复请求
	// 为降低难度与兼容性， DELETE,PUT等操作不用。

	@InFunLog(funName = "添加或者更新规则引擎", args = { 0 })
	@ApiOperation(value = "添加或者更新规则引擎", notes = "添加或者更新规则引擎")
	@RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
	public Resp add_(@RequestBody AmlRuleEngine amlRuleEngine) {
		if (null != amlRuleEngine.getId()) {
			AmlRuleEngine ruleEngineOld = amlRuleEngineRepo.findOne(amlRuleEngine.getId());
			if (null != ruleEngineOld) {
				amlRuleEngine.setDateCreate(ruleEngineOld.getDateCreate());
				amlRuleEngine.setDateModify(ruleEngineOld.getDateModify());
				amlRuleEngine.setApproved(ruleEngineOld.getApproved());
			}
		}
		if (null != amlRuleEngine.getId() && amlRuleEngine.getId() > 0) {
			amlRuleEngine.setDateModify(new Date());
		} else {
			amlRuleEngine.setDateCreate(new Date());
		}
		AmlRuleEngine RuleEngine = amlRuleEngineRepo.save(amlRuleEngine);
		return new Resp(RuleEngine, CodeDef.SUCCESS);
	}

	@InFunLog(funName = "删除规则", argNames = { "ID" })
	@ApiOperation(value = "删除规则", notes = "根据url的id来指定删除对象")
	@ApiImplicitParam(name = "id", value = "可疑规则ID", required = true, dataType = "Long")
	@RequestMapping(value = "/del", method = RequestMethod.GET)
	public Resp del_(@RequestParam("id") Long id) {
		amlRuleEngineRepo.delete(id);
		return new Resp(1);
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public Resp get_(@RequestParam("id") Long id) {
		AmlRuleEngine amlRuleEngine = amlRuleEngineRepo.findOne(id);
		return new Resp(amlRuleEngine, CodeDef.SUCCESS);
	}

	@RequestMapping(value = "/getScore", method = RequestMethod.GET)
	public Resp getScore(@RequestParam("id") Long id) {
		AmlRuleEngine amlRuleEngine = amlRuleEngineRepo.findOne(id);
		AmlRuleEngineScore score = new AmlRuleEngineScore();
		score.setRuleName(amlRuleEngine.getRuleName());
		score.setRuleNo(amlRuleEngine.getRuleNo());
		score.setRuleEngineId(amlRuleEngine.getId());
		return new Resp(score, CodeDef.SUCCESS);
	}
	/*
	 * @RequestMapping(value = "/addMore", method = RequestMethod.POST) public
	 * Resp addMore(@RequestBody Set<amlRuleEngine> AmlRuleEngine) { for
	 * (amlRuleEngine amlRuleEngine : AmlRuleEngine) { if
	 * (StringUtils.isEmpty(amlRuleEngine.getCompany())) {
	 * employee.setCompany(config.getCompany()); } }
	 * 
	 * Iterator<Employee> iter = amlRuleEngineRepo.save(employees).iterator();
	 * List<Long> ids = new ArrayList<>(); while (iter.hasNext()) {
	 * ids.add(iter.next().getId()); } return new Resp(ids.toArray(new Long[]
	 * {})); }
	 */

	// 删除规则
	@InFunLog(funName = "删除", args = { 0 }, argNames = { "ID" })
	@RequestMapping(value = "/delMore", method = RequestMethod.POST)
	public Resp delMore(@RequestBody Set<Long> ids) {
		Integer count = amlRuleEngineRepo.deleteMore(ids);
		return new Resp(ids.size()-count);
	}

	// 审核规则
	@RequestMapping(value = "/approveMore", method = RequestMethod.POST)
	public Resp approveMore(@RequestBody Set<Long> ids) {
		Integer count = amlRuleEngineRepo.approveMore(ids);
		return new Resp(count);
	}

	// 取消审核
	@RequestMapping(value = "/cancelApproveMore", method = RequestMethod.POST)
	public Resp cancelApproveMore(@RequestBody Set<Long> ids) {
		HashSet<Long> set = new HashSet<Long>();
		for (Long ruleEngineId : ids) {
			List<Long> list = amlRuleEngineScoreRepo.getByRuleEngineId(ruleEngineId);
			if(list.size()==0){
				set.add(ruleEngineId);
			}
		}
		if(set.size()>0){
			amlRuleEngineRepo.cancelApproveMore(set);
		}
		
		return new Resp(ids.size()-set.size());
	}

	/*
	 * @RequestMapping(value = "/modifyName", method = RequestMethod.POST)
	 * public Integer modifyName(@RequestBody Employee em) { return
	 * employeeRepo.setFixedNameFor(em.getId(), em.getName()); }
	 * 
	 * @RequestMapping(value = "/delByName", method = RequestMethod.GET) public
	 * Resp delByName(@RequestParam(value = "name") String name) { int count =
	 * employeeRepo.deleteByName(name); return new Resp(count); }
	 */

	@RequestMapping(value = "/getMore", method = RequestMethod.GET)
	public Resp getMore(@ModelAttribute PageQuery query, @ModelAttribute AmlRuleEngine ruleEngine) {
		PageRequest pageable = new PageRequest(query.getPindex(), query.getPcount(), query.getSortObj());
		Example<AmlRuleEngine> amlRuleEngine = Example.of(ruleEngine, ExampleMatcher.matchingAll());
		Page<AmlRuleEngine> page = amlRuleEngineRepo.findAll(amlRuleEngine, pageable);
		return new Resp(page);
	}

	@RequestMapping(value = "/getMoreForConfig", method = RequestMethod.GET)
	public Resp getMoreForConfig(@ModelAttribute PageQuery query, @ModelAttribute AmlRuleEngineScore score) {
		PageRequest pageable = new PageRequest(query.getPindex(), query.getPcount(), query.getSortObj());
		List<Long> ids = amlRuleEngineScoreRepo.getByTranFeaturesId(score.getTranFeaturesId());
		String ruleNo = score.getRuleNo();
		String ruleName = score.getRuleName();
		Page<AmlRuleEngine> page = null;
		page = amlRuleEngineRepo.findAll(new Specification<AmlRuleEngine>() {
			@Override
			public Predicate toPredicate(Root<AmlRuleEngine> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				if (StringUtils.isNotEmpty(ruleNo)) {
					list.add(cb.equal((root.get("ruleNo").as(String.class)), ruleNo));
				}
				if (StringUtils.isNotEmpty(ruleName)) {
					list.add(cb.equal((root.get("ruleName").as(String.class)), ruleName));
				}
				list.add(cb.equal((root.get("enabled").as(Byte.class)),1));
				list.add(cb.equal((root.get("approved").as(Byte.class)),1));
				list.add(cb.equal((root.get("type").as(Byte.class)),2));
				if (ids.size() > 0) {
					Iterator iterator = ids.iterator();
					In in = cb.in(root.get("id"));
					while (iterator.hasNext()) {
						in.value(iterator.next());
					}
					list.add(cb.not(in));
				}
				Predicate[] p = new Predicate[list.size()];
				query.where(cb.and(list.toArray(p)));
				return query.getRestriction();
			}
		}, pageable);
		return new Resp(page);
	}

	@RequestMapping(value = "/addAmlRuleEngineScore", method = RequestMethod.POST)
	public Resp addAmlRuleEngineScore(@RequestBody AmlRuleEngineScore ruleScore) {
		AmlRuleEngineScore ruleEngineScore = amlRuleEngineScoreRepo
				.findByRuleEngineIdAndTranFeaturesId(ruleScore.getRuleEngineId(),ruleScore.getTranFeaturesId());
		if (ruleEngineScore != null) {
			return new Resp(ruleEngineScore, CodeDef.ERROR);
		}
		AmlRuleEngineScore score = amlRuleEngineScoreRepo.save(ruleScore);
		return new Resp(score, CodeDef.SUCCESS);
	}

	@RequestMapping(value = "/getScoreMore", method = RequestMethod.GET)
	public Resp getScoreMore(@ModelAttribute PageQuery query, @ModelAttribute AmlRuleEngineScore score) {
		PageRequest pageable = new PageRequest(query.getPindex(), query.getPcount(), query.getSortObj());
		Example<AmlRuleEngineScore> amlRuleEngineScore = Example.of(score, ExampleMatcher.matchingAll());
		Page<AmlRuleEngineScore> page = amlRuleEngineScoreRepo.findAll(amlRuleEngineScore, pageable);
		return new Resp(page);
	}

	@RequestMapping(value = "/delScoreConfig", method = RequestMethod.GET)
	public Resp delConfig(@RequestParam("id") Long id) {
		amlRuleEngineScoreRepo.delete(id);
		return new Resp(CodeDef.SUCCESS);
	}

	@ApiOperation(value = "获取所有规则属性", notes = "获取所有规则属性")
	@RequestMapping(value = "/getAllPropCodeList", method = RequestMethod.GET)
	public Resp getPropCodeList() {
		List<SuspiciousPropDict> list = suspiciousPropDictRepo.findAll();
		return new Resp(list, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "获取所有可疑特征", notes = "获取所有可疑特征")
	@RequestMapping(value = "/getAllStcrCrcd", method = RequestMethod.GET)
	public Resp getAllStcrCrcd() {
		List<ModelPropDict> list = new ArrayList<ModelPropDict>();
		Set<String> set = new HashSet<String>();
		set.add("CRCD");
		list.addAll(dataDictService.getDictsByFields("T_AML_BIGAMOUNT", set));
		set = new HashSet<String>();
		set.add("STCR");
		list.addAll(dataDictService.getDictsByFields("T_AML_SUSPICIOUS", set));
		return new Resp(list, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "获取所有黑名单类型", notes = "获取所有黑名单类型")
	@RequestMapping(value = "/getAllHmdlx", method = RequestMethod.GET)
	public Resp getAllHmdlx() {
		List<ModelPropDict> list = new ArrayList<ModelPropDict>();
		Set<String> set = new HashSet<String>();
		set.add("LISKTYPE");
		list.addAll(dataDictService.getDictsByFields("T_BASE_KYC_BLACKWHITELIST", set));
		return new Resp(list, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "获取所有黑名单来源", notes = "获取所有黑名单来源")
	@RequestMapping(value = "/getAllMdly", method = RequestMethod.GET)
	public Resp getAllMdly() {
		List<ModelPropDict> list = new ArrayList<ModelPropDict>();
		Set<String> set = new HashSet<String>();
		set.add("MDLY");
		list.addAll(dataDictService.getDictsByFields("T_BASE_KYC_BLACKWHITELIST", set));
		return new Resp(list, CodeDef.SUCCESS);
	}
}
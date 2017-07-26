package net.riking.web.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.core.entity.MultipleChoiceCustom;
import net.riking.core.entity.Resp;
import net.riking.core.entity.model.ModelPropDict;
import net.riking.core.log.InFunLog;
import net.riking.core.log.InModLog;
import net.riking.core.service.repo.ModelPropdictRepo;
import net.riking.entity.PageQuery;
import net.riking.entity.model.AmlRuleEngineScore;
import net.riking.entity.model.TranFeatures;
import net.riking.service.repo.AmlRuleEngineScoreRepo;
import net.riking.service.repo.TranFeaturesRepo;

/**
 * Created by bing.xun on 2017/5/24.
 */
@InModLog(modName = "涉罪类型")
@RestController
@RequestMapping(value = "/tranFeatures")
public class TranFeaturesController {

	@Autowired
	TranFeaturesRepo crimeTypeRepo2;
	
	@Autowired
	AmlRuleEngineScoreRepo amlRuleEngineScoreRepo;

	@Autowired
	ModelPropdictRepo modelPropdictRepo;

	@InFunLog(funName = "添加或者更新涉罪类型", args = { 0 })
	@ApiOperation(value = "添加或者更新涉罪类型", notes = "POST-@CrimeType")
	@RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
	public Resp add_(@RequestBody TranFeatures crimeType) {
		crimeType.setApproved(0);
		TranFeatures ct = crimeTypeRepo2.save(crimeType);
		return new Resp(ct, CodeDef.SUCCESS);
	}

	@InFunLog(funName = "删除涉罪类型", argNames = { "ID" })
	@ApiOperation(value = "删除涉罪类型", notes = "根据url的id来指定删除对象")
	@ApiImplicitParam(name = "id", value = "员工ID", required = true, dataType = "Long")
	@RequestMapping(value = "/del", method = RequestMethod.GET)
	public Resp del_(@RequestParam("id") Long id) {
		crimeTypeRepo2.delete(id);
		return new Resp(1);
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public Resp get_(@RequestParam("id") Long id) {
		TranFeatures crimeType = crimeTypeRepo2.findOne(id);
		return new Resp(crimeType, CodeDef.SUCCESS);
	}
	
	// 审核规则
		@RequestMapping(value = "/approveMore", method = RequestMethod.POST)
		public Resp approveMore(@RequestBody Set<Long> ids) {
			Integer count = crimeTypeRepo2.approveMore(ids);
			return new Resp(count);
		}

		// 取消审核
		@RequestMapping(value = "/cancelApproveMore", method = RequestMethod.POST)
		public Resp cancelApproveMore(@RequestBody Set<Long> ids) {
			Integer count = crimeTypeRepo2.cancelApproveMore(ids);
			return new Resp(count);
		}

	@InFunLog(funName = "删除", args = { 0 }, argNames = { "ID" })
	@RequestMapping(value = "/delMore", method = RequestMethod.POST)
	public Resp delMore(@RequestBody Set<Long> ids) {
		List<TranFeatures> list = crimeTypeRepo2.findAll(ids);
		HashSet<Long> set = new HashSet<Long>();
		for (TranFeatures tranFeatures : list) {
			if(tranFeatures.getApproved()==0){
				set.add(tranFeatures.getId());
			}
		}
		if(set.size()>0){
			amlRuleEngineScoreRepo.deleteByTranFeaturesId(set);
			crimeTypeRepo2.deleteByIds(set);
		}
		return new Resp(ids.size()-set.size(),CodeDef.SUCCESS);
	}

	@RequestMapping(value = "/getMore", method = RequestMethod.GET)
	public Resp getMore(@ModelAttribute PageQuery query, @ModelAttribute TranFeatures ct) {
		PageRequest pageable = new PageRequest(query.getPindex(), query.getPcount(), query.getSortObj());
		Example<TranFeatures> example = Example.of(ct, ExampleMatcher.matchingAll());
		Page<TranFeatures> page = crimeTypeRepo2.findAll(example, pageable);
		
		for (TranFeatures tranFeature : page.getContent()) {
			List<AmlRuleEngineScore> scores = amlRuleEngineScoreRepo.findByTranFeaturesId(tranFeature.getId());
			StringBuilder sb = new StringBuilder();
			boolean isFirst = true;
			for (AmlRuleEngineScore score : scores) {
				if(isFirst){
					sb.append(score.getRuleNo());
					isFirst = false;
				}else{
					sb.append(","+score.getRuleNo());
				}
			}
			tranFeature.setJcbz(sb.toString());
		}
		return new Resp(page);
	}

	@RequestMapping(value = "/getSzlxdm", method = RequestMethod.GET)
	public Resp getSzlxdm(@RequestParam(value = "prop", required = false) String prop) {
		HashSet<String> set = new HashSet<String>();
		set.add("SZLXDM");
		List<ModelPropDict> list = modelPropdictRepo.getDatas("T_AML_CRIME_TYPE", set);
		MultipleChoiceCustom choice;
		List<MultipleChoiceCustom> multipleChoiceCustoms = new ArrayList<MultipleChoiceCustom>();
		for (ModelPropDict dict : list) {
			choice = new MultipleChoiceCustom();
			choice.setKey(dict.getKe());
			choice.setValue(dict.getValu());
			choice.setProp(prop);
			multipleChoiceCustoms.add(choice);
		}
		return new Resp(multipleChoiceCustoms, CodeDef.SUCCESS);
	}

	@RequestMapping(value = "/getMdly", method = RequestMethod.GET)
	public Resp getMdly(@RequestParam(value = "prop", required = false) String prop) {
		HashSet<String> set = new HashSet<String>();
		set.add("MDLY");
		List<ModelPropDict> list = modelPropdictRepo.getDatas("T_BASE_KYC_BLACKWHITELIST", set);
		MultipleChoiceCustom choice;
		List<MultipleChoiceCustom> multipleChoiceCustoms = new ArrayList<MultipleChoiceCustom>();
		for (ModelPropDict dict : list) {
			choice = new MultipleChoiceCustom();
			choice.setKey(dict.getKe());
			choice.setValue(dict.getValu());
			choice.setProp(prop);
			multipleChoiceCustoms.add(choice);
		}
		return new Resp(multipleChoiceCustoms, CodeDef.SUCCESS);
	}

	@RequestMapping(value = "/getBlackProps", method = RequestMethod.GET)
	public Resp getBlackProps(@RequestParam(value = "prop", required = false) String prop) {
		HashSet<String> set = new HashSet<String>();
		set.add("PROP");
		List<ModelPropDict> list = modelPropdictRepo.getDatas("T_BASE_KYC_BLACKWHITELIST", set);
		MultipleChoiceCustom choice;
		List<MultipleChoiceCustom> multipleChoiceCustoms = new ArrayList<MultipleChoiceCustom>();
		for (ModelPropDict dict : list) {
			choice = new MultipleChoiceCustom();
			choice.setKey(dict.getKe());
			choice.setValue(dict.getValu());
			choice.setProp(prop);
			multipleChoiceCustoms.add(choice);
		}
		return new Resp(multipleChoiceCustoms, CodeDef.SUCCESS);
	}

	/*
	 * @RequestMapping(value = "/getAllCode", method = RequestMethod.GET) public
	 * Resp getAllCode(@RequestParam(value = "prop", required = false) String
	 * prop){ List<CrimeType2> list = crimeTypeRepo2.findAll(); List<EnumCustom>
	 * enumKeyValues = new ArrayList<EnumCustom>(); EnumCustom enumCustom;
	 * for(CrimeType2 ct : list){ enumCustom = new EnumCustom();
	 * enumCustom.setKey(ct.getSzkyjyxwdm());
	 * enumCustom.setValue(ct.getSzkyjyxwdm()+"-"+ct.getSzkyjyxw()); //
	 * 为了区分多个枚举，此字段为框架自动传入 enumCustom.setProp(prop);
	 * enumKeyValues.add(enumCustom); } return new Resp(enumKeyValues); }
	 */
}

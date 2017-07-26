package net.riking.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.core.entity.Resp;
import net.riking.core.entity.model.ModelPropDict;
import net.riking.core.service.DataDictService;
import net.riking.entity.PageQuery;
import net.riking.entity.model.RiskFactorConfig;
import net.riking.entity.risk.RiskFactor;
import net.riking.service.repo.RiskFactorConfigRepo;
import net.riking.service.repo.RiskFactorRepo;

@RestController
@RequestMapping(value = "/riskFactor")
public class RiskFactorController {

	@Autowired
	RiskFactorRepo riskFactorRepo;

	@Autowired
	RiskFactorConfigRepo riskFactorConfigRepo;

	@Autowired
	DataDictService dataDictService;

	@ApiOperation(value = "增加或修改定性因子", notes = "POST-@RiskFactor")
	@RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
	public Resp saveOrUpdate(@RequestBody RiskFactor riskFactor) {
		if (riskFactor.getId() == null || riskFactor.getId() == 0) {
			if (StringUtils.isEmpty(riskFactor.getFactorName())) {
				return new Resp(null, CodeDef.EMP.NAME_IS_NULL);
			} else {
				String factorCode = UUID.randomUUID().toString();
				riskFactor.setFactorCode(factorCode);
				RiskFactor factor = riskFactorRepo.save(riskFactor);
				return new Resp(factor, CodeDef.SUCCESS);
			}
		} else {
			if (StringUtils.isEmpty(riskFactor.getFactorName())) {
				return new Resp(null, CodeDef.EMP.NAME_IS_NULL);
			} else {
				Long id = riskFactor.getId();
				RiskFactor r = riskFactorRepo.findById(id);
				r.setParentFactorCode(riskFactor.getParentFactorCode());
				r.setFactorName(riskFactor.getFactorName());
				r.setWeights(riskFactor.getWeights());
				r.setCorptrnProp(riskFactor.getCorptrnProp());
				RiskFactor factor = riskFactorRepo.save(r);
				return new Resp(factor, CodeDef.SUCCESS);
			}
		}
	}

	@RequestMapping(value = "/getMore", method = RequestMethod.GET)
	public Resp getMore(@ModelAttribute PageQuery query, @ModelAttribute RiskFactor riskFactor) {
		PageRequest pageable = new PageRequest(query.getPindex(), query.getPcount(), query.getSortObj());
		String type = riskFactor.getRisktype();
		if (type==null) {
			type="1";
		}
		//Example<RiskFactor> example = Example.of(riskFactor, ExampleMatcher.matchingAll());
		Page<RiskFactor> page = riskFactorRepo.findByRisktypeAndParentFactorCodeNot(type,"0", pageable);
		return new Resp(page);
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public Resp get_(@RequestParam("id") Long id) {
		RiskFactor riskFactor = riskFactorRepo.findOne(id);
		return new Resp(riskFactor, CodeDef.SUCCESS);
	}

	@RequestMapping(value = "/getParentFactorCode", method = RequestMethod.GET)
	public Resp getParentFactorCode() {
		List<RiskFactor> all = riskFactorRepo.findAll();
		Map<String, String> map = new HashMap<String, String>();
		for (RiskFactor r : all) {
			if (r.getParentFactorCode().equals("0")) {
				if (!map.containsKey(r.getFactorCode())) {
					map.put(r.getFactorCode(), r.getFactorName());
				}
			}
		}
		List<ModelPropDict> list = new ArrayList<ModelPropDict>();
		Set<Entry<String, String>> set = map.entrySet();
		Iterator<Entry<String, String>> it = set.iterator();
		while (it.hasNext()) {
			Entry<String, String> entry = it.next();
			ModelPropDict dict = new ModelPropDict();
			dict.setKey(entry.getKey());
			dict.setValu(entry.getValue());
			dict.setField("parentFactorCode");
			dict.setClazz("T_BASE_KYC_RiskFactor");
			list.add(dict);
		}
		return new Resp(list, CodeDef.SUCCESS);
	}

	@RequestMapping(value = "/getWeight", method = RequestMethod.GET)
	public Resp getWeight() {
		Integer weight = 0;
		List<RiskFactor> list = riskFactorRepo.findMore();
		for (RiskFactor riskFactor : list) {
			if (!riskFactor.getParentFactorCode().equals("0")) {
				weight += riskFactor.getWeights();
			}
		}
		return new Resp(weight, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "删除定性因子", notes = "根据url的id来指定删除对象")
	@ApiImplicitParam(name = "id", value = "风险因子ID", required = true, dataType = "Long")
	@RequestMapping(value = "/del", method = RequestMethod.GET)
	public Resp del_(@RequestParam("id") Long id) {
		RiskFactor riskFactor = riskFactorRepo.getOne(id);
		riskFactorConfigRepo.deleteByFactorCode(riskFactor.getFactorCode());
		riskFactorRepo.delete(id);
		return new Resp(1);
	}

	@RequestMapping(value = "/delMore", method = RequestMethod.POST)
	public Resp delMore(@RequestBody Set<Long> ids) {
		Integer count = 0;
		for (Long id : ids) {
			riskFactorRepo.delete(id);
			count += 1;
		}
		return new Resp(count);
	}

	@ApiOperation(value = "增加风险因子", notes = "POST-@RiskFactorConfig")
	@RequestMapping(value = "/addOrUpdatees", method = RequestMethod.POST)
	public Resp saveOrUpdatees(@RequestBody RiskFactorConfig riskFactorConfig) {
		String factorCodeValue = UUID.randomUUID().toString();
		riskFactorConfig.setFactorCodeValue(factorCodeValue);
		RiskFactorConfig riskFactorConfigs = riskFactorConfigRepo.save(riskFactorConfig);
		return new Resp(riskFactorConfigs, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "编辑风险因子", notes = "POST-@RiskFactorConfig")
	@RequestMapping(value = "/updatees", method = RequestMethod.POST)
	public Resp updatees(@RequestBody RiskFactorConfig riskFactorConfig) {
		RiskFactorConfig riskFactorConfiges = riskFactorConfigRepo.findOne(riskFactorConfig.getId());
		riskFactorConfiges.setFactorCodeName(riskFactorConfig.getFactorCodeName());
		riskFactorConfiges.setScore(riskFactorConfig.getScore());
		RiskFactorConfig riskFactorConfigs = riskFactorConfigRepo.save(riskFactorConfiges);
		return new Resp(riskFactorConfigs, CodeDef.SUCCESS);
	}

	@RequestMapping(value = "/getMorees", method = RequestMethod.GET)
	public Resp getMorees(@ModelAttribute PageQuery query, @ModelAttribute RiskFactorConfig riskFactorConfig) {
		Sort sort = new Sort(Direction.ASC, "score");
		PageRequest pageable = new PageRequest(query.getPindex(), query.getPcount(), sort);
		RiskFactor riskFactors = riskFactorRepo.findOne(riskFactorConfig.getId());
		String factorCode = riskFactors.getFactorCode();
		if (factorCode == null) {
		} else {

			riskFactorConfig.setFactorCode(factorCode);
		}
		riskFactorConfig.setId(null);
		Example<RiskFactorConfig> example = Example.of(riskFactorConfig, ExampleMatcher.matchingAll());
		Page<RiskFactorConfig> page = riskFactorConfigRepo.findAll(example, pageable);
		return new Resp(page);
	}
	// .withMatcher("factorCode", GenericPropertyMatchers.
	// exact()).withIgnorePaths("id","rank","score","factorCodeValue","factorCodeName")

	@RequestMapping(value = "/getes", method = RequestMethod.GET)
	public Resp get_es(@RequestParam("id") Long id) {
		RiskFactorConfig riskFactorConfig = riskFactorConfigRepo.findOne(id);
		return new Resp(riskFactorConfig, CodeDef.SUCCESS);
	}

	@RequestMapping(value = "/getees", method = RequestMethod.GET)
	public Resp get_ees(@RequestParam("id") Long id) {
		RiskFactor riskFactor = riskFactorRepo.findOne(id);
		RiskFactorConfig riskFactorConfig = new RiskFactorConfig();
		riskFactorConfig.setFactorCode(riskFactor.getFactorCode());
		return new Resp(riskFactorConfig, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "删除定性因子", notes = "根据url的id来指定删除对象")
	@ApiImplicitParam(name = "id", value = "风险因子ID", required = true, dataType = "Long")
	@RequestMapping(value = "/deles", method = RequestMethod.GET)
	public Resp del_es(@RequestParam("id") Long id) {
		riskFactorConfigRepo.delete(id);
		return new Resp(1);
	}

	@RequestMapping(value = "/delMorees", method = RequestMethod.POST)
	public Resp delMorees(@RequestBody Set<Long> ids) {
		Integer count = 0;
		for (Long id : ids) {
			riskFactorConfigRepo.delete(id);
			count += 1;
		}
		return new Resp(count);
	}
}

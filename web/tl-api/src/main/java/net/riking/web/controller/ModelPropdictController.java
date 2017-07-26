package net.riking.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.riking.config.CodeDef;
import net.riking.core.entity.EnumCustom;
import net.riking.core.entity.Resp;
import net.riking.core.entity.model.ModelPropDict;
import net.riking.core.service.DataDictService;
import net.riking.entity.model.BaseCorpCust;
import net.riking.entity.model.BaseIndvCust;
import net.riking.service.repo.BaseAifRepo;
import net.riking.service.repo.BaseCorpCustRepo;
import net.riking.service.repo.BaseIndvCustRepo;
import net.riking.service.repo.BigAmountRepo;

/**
 * Created by bing.xun on 2017/3/30.
 */
@RestController
@RequestMapping(value = "/modelPropdict")
public class ModelPropdictController {
	@Autowired
	DataDictService dataDictService;
	@Autowired
	BaseCorpCustRepo baseCorpCustRepo;
	@Autowired
	BaseIndvCustRepo baseIndvCustRepo;
	@Autowired
	BaseAifRepo baseAifRepo;
	@Autowired
	BigAmountRepo bigAmountRepo;

	@RequestMapping(value = "/{tableName}", method = RequestMethod.POST)
	public Resp getModelAttrsInfo(@PathVariable(name = "tableName") String tableName, @RequestBody Set<String> fields)
			throws Exception {
		List<ModelPropDict> list = dataDictService.getDictsByFields(tableName, fields);
		return new Resp(list);
	}

	@RequestMapping(value = "/getCtid", method = RequestMethod.GET)
	public Resp getCtid(@RequestParam(value = "prop", required = false) String prop,
			@RequestParam(value = "keyword", required = false) String keyword) {
		List<EnumCustom> enumKeyValues = new ArrayList<EnumCustom>();
		List<BaseCorpCust> list = baseCorpCustRepo.findKhzjhmLike(keyword + "%");
		for (BaseCorpCust cust : list) {
			EnumCustom enumCustom = new EnumCustom();
			enumCustom.setKey(cust.getId() + "," + cust.getKhzjhm());
			enumCustom.setValue(cust.getKhzjhm());
			enumCustom.setProp(prop);
			enumKeyValues.add(enumCustom);
		}
		List<BaseIndvCust> list2 = baseIndvCustRepo.findZjhmLike(keyword + "%");
		for (BaseIndvCust cust : list2) {
			EnumCustom enumCustom = new EnumCustom();
			enumCustom.setKey(cust.getId() + "," + cust.getZjhm());
			enumCustom.setValue(cust.getZjhm());
			enumCustom.setProp(prop);
			enumKeyValues.add(enumCustom);
		}
		return new Resp(enumKeyValues);
	}

	@RequestMapping(value = "/getCtac", method = RequestMethod.GET)
	public Resp getCtac(@RequestParam(value = "prop", required = false) String prop,
			@RequestParam(value = "keyword", required = false) String keyword) {
		List<EnumCustom> enumKeyValues = new ArrayList<EnumCustom>();
		List<String> list2 = baseAifRepo.findLikeZh(keyword + "%");
		for (String zh : list2) {
			EnumCustom enumCustom = new EnumCustom();
			enumCustom.setKey(zh + "," + zh);
			enumCustom.setValue(zh);
			enumCustom.setProp(prop);
			enumKeyValues.add(enumCustom);
		}
		return new Resp(enumKeyValues);
	}

	@RequestMapping(value = "/getTcac", method = RequestMethod.GET)
	public Resp getTcac(@RequestParam(value = "prop", required = false) String prop,
			@RequestParam(value = "keyword", required = false) String keyword) {
		List<EnumCustom> enumKeyValues = new ArrayList<EnumCustom>();
		List<String> list2 = bigAmountRepo.findLikeTcac(keyword + "%");
		for (String tcac : list2) {
			EnumCustom enumCustom = new EnumCustom();
			enumCustom.setKey(tcac + "," + tcac);
			enumCustom.setValue(tcac);
			enumCustom.setProp(prop);
			enumKeyValues.add(enumCustom);
		}
		return new Resp(enumKeyValues);
	}

	@RequestMapping(value = "/getDemoEnum", method = RequestMethod.GET)
	public Resp getDemoEnum(@RequestParam(value = "prop", required = false) String prop) {
		List<EnumCustom> enumKeyValues = new ArrayList<EnumCustom>();
		Set<String> set = new HashSet<String>();
		set.add("KHLX");
		List<ModelPropDict> list = dataDictService.getDictsByFields("T_MODEL_AML_CORPTRN", set);
		for (ModelPropDict dict : list) {
			EnumCustom enumCustom = new EnumCustom();
			enumCustom.setKey(dict.getKe());
			enumCustom.setValue(dict.getValu());
			enumCustom.setProp(prop);
			enumKeyValues.add(enumCustom);
		}
		return new Resp(enumKeyValues);
	}

	@RequestMapping(value = "/getAddF", method = RequestMethod.GET)
	public Resp getAddF(@RequestParam(value = "prop", required = false) String prop,
			@RequestParam(value = "keyword", required = false) String keyword) {
		List<EnumCustom> enumKeyValues = new ArrayList<EnumCustom>();
		Set<String> set = new HashSet<String>();
		set.add("COUNTRY");
		List<ModelPropDict> list = dataDictService.getDictsByFields("T_AML_SUSPICIOUS", set);
		for (ModelPropDict dict : list) {
			if (dict.getValu().toLowerCase().contains(keyword.toLowerCase())) {
				EnumCustom enumCustom = new EnumCustom();
				enumCustom.setKey(dict.getKe());
				enumCustom.setValue(dict.getValu());
				enumCustom.setProp(prop);
				enumKeyValues.add(enumCustom);
			}
		}

		return new Resp(enumKeyValues);
	}

	@RequestMapping(value = "/getAddS", method = RequestMethod.GET)
	public Resp getAddS(@RequestParam(value = "key", required = false) String key,
			@RequestParam(value = "prop", required = false) String prop,
			@RequestParam(value = "keyword", required = false) String keyword) {
		List<EnumCustom> enumKeyValues = new ArrayList<EnumCustom>();
		String[] codeArr = { "CHN", "Z01", "Z02", "Z03" };
		List<String> codeList = Arrays.asList(codeArr);
		if (codeList.contains(key) || StringUtils.isEmpty(key) || key.contains("@")) {
			Set<String> set = new HashSet<String>();
			set.add("FIRC");
			List<ModelPropDict> list = dataDictService.getDictsByFields("T_BASE_AREA", set);
			for (ModelPropDict dict : list) {
				boolean flag = false;
				if (keyword.contains("-")) {
					String[] keys = keyword.split("-");
					if (dict.getKe().startsWith(keys[0])
							|| dict.getValu().toLowerCase().contains(keys[1].toLowerCase())) {
						flag = true;
					}
				} else if (dict.getKe().startsWith(keyword)
						|| dict.getValu().toLowerCase().contains(keyword.toLowerCase())) {
					flag = true;
				}
				if (flag) {
					EnumCustom enumCustom = new EnumCustom();
					enumCustom.setKey(dict.getKe());
					enumCustom.setValue(dict.getKe() + "-" + dict.getValu());
					enumCustom.setProp(prop);
					enumKeyValues.add(enumCustom);
				}
			}
			if (StringUtils.isEmpty(key) || key.contains("@")) {
				EnumCustom enumCustom = new EnumCustom();
				enumCustom.setKey("000000");
				enumCustom.setValue("000000");
				enumCustom.setProp(prop);
				enumKeyValues.add(enumCustom);
			}
		} else if (StringUtils.isNotEmpty(key) && !key.contains("@")) {
			EnumCustom enumCustom = new EnumCustom();
			enumCustom.setKey("000000");
			enumCustom.setValue("000000");
			enumCustom.setProp(prop);
			enumKeyValues.add(enumCustom);
		}
		return new Resp(enumKeyValues);
	}

	@RequestMapping(value = "/getTstpF", method = RequestMethod.GET)
	public Resp getTstpF(@RequestParam(value = "prop", required = false) String prop) {
		List<EnumCustom> enumKeyValues = new ArrayList<EnumCustom>();
		Set<String> set = new HashSet<String>();
		set.add("TSTP");
		List<ModelPropDict> list = dataDictService.getDictsByFields("T_BASE_TRANS", set);
		for (ModelPropDict dict : list) {
			EnumCustom enumCustom = new EnumCustom();
			enumCustom.setKey(dict.getKe());
			enumCustom.setValue(dict.getValu());
			enumCustom.setProp(prop);
			enumKeyValues.add(enumCustom);
		}
		return new Resp(enumKeyValues);
	}

	@RequestMapping(value = "/getTstpS", method = RequestMethod.GET)
	public Resp getTstpS(@RequestParam(value = "key", required = false) String key,
			@RequestParam(value = "prop", required = false) String prop) {
		List<EnumCustom> enumKeyValues = new ArrayList<EnumCustom>();
		if (StringUtils.isNotEmpty(key)) {
			Set<String> set = new HashSet<String>();
			set.add(key);
			List<ModelPropDict> list = dataDictService.getDictsByFields("T_BASE_TRANS", set);
			for (ModelPropDict dict : list) {
				EnumCustom enumCustom = new EnumCustom();
				enumCustom.setKey(dict.getKe());
				enumCustom.setValue(dict.getValu());
				enumCustom.setProp(prop);
				enumKeyValues.add(enumCustom);
			}
		} else {
			Set<String> set = new HashSet<String>();
			set.add("TSTP");
			List<ModelPropDict> list = dataDictService.getDictsByFields("T_BASE_TRANS", set);

			Set<String> set2 = new HashSet<String>();
			for (ModelPropDict dict : list) {
				set2.add(dict.getKe());
			}
			List<ModelPropDict> list2 = dataDictService.getDictsByFields("T_BASE_TRANS", set2);
			for (ModelPropDict dict : list2) {
				EnumCustom enumCustom = new EnumCustom();
				enumCustom.setKey(dict.getKe());
				enumCustom.setValue(dict.getValu());
				enumCustom.setProp(prop);
				enumKeyValues.add(enumCustom);
			}
		}
		return new Resp(enumKeyValues);
	}

	@RequestMapping(value = "/getTstpT", method = RequestMethod.GET)
	public Resp getTstpT(@RequestParam(value = "key", required = false) String key,
			@RequestParam(value = "prop", required = false) String prop) {

		List<EnumCustom> enumKeyValues = new ArrayList<EnumCustom>();
		if (StringUtils.isEmpty(key) || key.length() < 4) {
			Set<String> set = new HashSet<String>();
			set.add("001");
			List<ModelPropDict> list = dataDictService.getDictsByFields("T_BASE_TRANS", set);
			for (ModelPropDict dict : list) {
				EnumCustom enumCustom = new EnumCustom();
				enumCustom.setKey(dict.getKe());
				enumCustom.setValue(dict.getValu());
				enumCustom.setProp(prop);
				enumKeyValues.add(enumCustom);
			}
		}

		return new Resp(enumKeyValues);
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public Resp get(@RequestParam(value = "table") String table, @RequestParam(value = "field") String field,
			@RequestParam(value = "key") String key) {
		ModelPropDict dict = dataDictService.getDict(table, field, key);
		return new Resp(dict).setCode(CodeDef.SUCCESS);
	}

	@RequestMapping(value = "/getDict", method = RequestMethod.GET)
	public Resp getDict(@RequestParam("tableName") String tableName, @RequestParam("field") String field)
			throws Exception {
		List<ModelPropDict> list = dataDictService.getDicts(tableName, field);
		return new Resp(list, CodeDef.SUCCESS);
	}
}

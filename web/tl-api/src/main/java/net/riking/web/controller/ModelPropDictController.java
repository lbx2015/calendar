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

import net.riking.core.entity.EnumCustom;
import net.riking.core.entity.Resp;
import net.riking.core.entity.model.ModelPropDict;
import net.riking.core.service.DataDictService;
import net.riking.core.service.repo.ModelPropdictRepo;

@RestController
@RequestMapping(value = "/modelPropdict")
public class ModelPropDictController {
	
	@Autowired
	ModelPropdictRepo modelPropdictRepo;
	
	@Autowired
	DataDictService dataDictService;
	
	@RequestMapping(value = "/{tableName}", method = RequestMethod.POST)
	public Resp getModelAttrsInfo(@PathVariable(name = "tableName") String tableName, @RequestBody Set<String> fields)
			throws Exception {
		List<ModelPropDict> list = dataDictService.getDictsByFields(tableName, fields);
		return new Resp(list);
	}
	
	@RequestMapping(value = "/getEmailSuffix", method = RequestMethod.GET)
	public Resp getEmailSuffix(@RequestParam(value = "prop", required = false) String prop){
		HashSet<String> set = new HashSet<String>();
		set.add("EMAILSUFFIX");
		List<ModelPropDict> datas = modelPropdictRepo.getDatas("T_APP_USER",set);
		List<EnumCustom> enumKeyValues = new ArrayList<EnumCustom>();
		for (ModelPropDict data : datas) {
			EnumCustom enumCustom = new EnumCustom();
			enumCustom.setKey(data.getKe());
			enumCustom.setValue(data.getValu());
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
		set.add("");
		List<ModelPropDict> list = dataDictService.getDictsByFields("T_APP_USER", set);
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
		if (StringUtils.isEmpty(key)) {
			Set<String> set = new HashSet<String>();
			set.add("");
			List<ModelPropDict> list = dataDictService.getDictsByFields("T_APP_USER", set);
			for (ModelPropDict dict : list) {
				EnumCustom enumCustom = new EnumCustom();
				enumCustom.setKey(dict.getKe());
				enumCustom.setValue(dict.getKe() + "-" + dict.getValu());
				enumCustom.setProp(prop);
				enumKeyValues.add(enumCustom);
			}
		} else {
			Set<String> set = new HashSet<String>();
			set.add(key);
			List<ModelPropDict> list = dataDictService.getDictsByFields("T_APP_USER", set);
			for (ModelPropDict dict : list) {
				EnumCustom enumCustom = new EnumCustom();
				enumCustom.setKey(dict.getKe());
				enumCustom.setValue(dict.getKe() + "-" + dict.getValu());
				enumCustom.setProp(prop);
				enumKeyValues.add(enumCustom);
			}
		}
		return new Resp(enumKeyValues);
	}
}

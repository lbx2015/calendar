package net.riking.web.controller;

import java.util.ArrayList;
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
import net.riking.core.service.repo.ModelPropdictRepo;
/**
 * 
 * @author you.fei
 * @version crateTime：2017年8月5日 下午4:35:08
 * @used TODO
 * web端字典操作
 */
@RestController
@RequestMapping(value = "/modelPropDict")
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
		List<ModelPropDict> datas = dataDictService.getDicts("T_APP_USER","EMAILSUFFIX");
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
		List<ModelPropDict> list = dataDictService.getDicts("T_APP_USER", "SF");
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
		List<ModelPropDict> list = dataDictService.getDicts("T_APP_USER", key);
		if (StringUtils.isEmpty(key)) {
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
		} else {
			for (ModelPropDict dict : list) {
				if (dict.getValu().toLowerCase().contains(keyword.toLowerCase())) {
					EnumCustom enumCustom = new EnumCustom();
					enumCustom.setKey(dict.getKe());
					enumCustom.setValue(dict.getValu());
					enumCustom.setProp(prop);
					enumKeyValues.add(enumCustom);
				}
			}
		}
		return new Resp(enumKeyValues);
	}
	
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public Resp get(@RequestParam(value = "table") String table, @RequestParam(value = "field") String field,
			@RequestParam(value = "key") String key) {
		ModelPropDict dict = dataDictService.getDict(table, field, key);
		if(dict!=null){
			return new Resp(dict).setCode(CodeDef.SUCCESS);
		}
		return new Resp().setCode(CodeDef.SUCCESS);
	}
	
	@RequestMapping(value = "/getPhoneType", method = RequestMethod.GET)
	public Resp getPhoneType() throws Exception {
		List<ModelPropDict> list = dataDictService.getDicts("T_CTRY_HDAY_CRCY", "PHONETYPE");
		return new Resp(list);
	}
}

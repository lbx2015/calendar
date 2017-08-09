package net.riking.web.appInterface;

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
import net.riking.core.entity.Resp;
import net.riking.core.entity.model.ModelPropDict;
import net.riking.core.service.repo.ModelPropdictRepo;
import net.riking.service.SysDataService;
/**
 * App数据字典接口
 * @author you.fei
 * @version crateTime：2017年8月5日 下午4:33:43
 * @used TODO
 */
@RestController
@RequestMapping(value = "/modelPropDictApp")
public class ModelPropDictServer {
	
	@Autowired
	ModelPropdictRepo modelPropdictRepo;
	
//	@Autowired
//	DataDictService dataDictService;
	@Autowired
	SysDataService sysDataservice;
	
	@RequestMapping(value = "/{tableName}", method = RequestMethod.POST)
	public Resp getModelAttrsInfo(@PathVariable(name = "tableName") String tableName, @RequestBody Set<String> fields)
			throws Exception {
		List<ModelPropDict> list = sysDataservice.getDictsByFields(tableName, fields);
		return new Resp(list);
	}
	
	
	@RequestMapping(value = "/getAddF", method = RequestMethod.POST)
	public Resp getAddF(@RequestParam(value = "prop", required = false) String prop,
			@RequestParam(value = "keyword") String keyword) {
		List<ModelPropDict> enumKeyValues = new ArrayList<ModelPropDict>();
		List<ModelPropDict> list = sysDataservice.getDicts("T_APP_USER", "SF");
		for (ModelPropDict dict : list) {
			if (dict.getValu().toLowerCase().contains(keyword.toLowerCase())) {
				enumKeyValues.add(dict);
			}
		}

		return new Resp(enumKeyValues);
	}

	@RequestMapping(value = "/getAddS", method = RequestMethod.POST)
	public Resp getAddS(@RequestParam(value = "key", required = false) String key,
			@RequestParam(value = "prop", required = false) String prop,
			@RequestParam(value = "keyword") String keyword) {
		List<ModelPropDict> enumKeyValues = new ArrayList<ModelPropDict>();
		List<ModelPropDict> list = sysDataservice.getDicts("T_APP_USER", key);
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
					enumKeyValues.add(dict);
				}
			}
		} else {
			for (ModelPropDict dict : list) {
				if (dict.getValu().toLowerCase().contains(keyword.toLowerCase())) {
					enumKeyValues.add(dict);
				}
			}
		}
		return new Resp(enumKeyValues);
	}
	
	@RequestMapping(value = "/get", method = RequestMethod.POST)
	public Resp get(@RequestParam(value = "table") String table, @RequestParam(value = "field") String field,
			@RequestParam(value = "key") String key) {
		ModelPropDict dict = sysDataservice.getDict(table, field, key);
		if(dict!=null){
			return new Resp(dict).setCode(CodeDef.SUCCESS);
		}
		return new Resp().setCode(CodeDef.SUCCESS);
	}
}

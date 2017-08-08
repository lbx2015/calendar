package net.riking.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.riking.core.entity.model.ModelPropDict;
import net.riking.core.service.repo.ModelPropdictRepo;
import net.riking.core.utils.ExceptUtils;
import net.riking.service.SysDataService;
import net.riking.util.JedisUtil;
import net.riking.util.SerializeUtil;

/***
 * 系统所有字典或常量数据接口实现
 * @author you.fei
 * @version crateTime：2017年8月5日 下午7:08:44
 * @used TODO
 */
@Service("sysDataService")
public class SysDataServiceImpl implements SysDataService {
	Logger logger = LogManager.getLogger(getClass());
	
	@Autowired
	ModelPropdictRepo modelPropdictRepo;
	
	@Autowired
	JedisUtil jedisUtil;
	
	public static final String SYS_DICT = "DICT";
	
	public void initData(){
		Map<String, Map<String, List<ModelPropDict>>> map = findAllSysDict();
		logger.info("initData=======================================================");
		byte[] b = SerializeUtil.serialize(map);
		JedisUtil.Strings strings=jedisUtil.new Strings();
		strings.set(SerializeUtil.serialize(SYS_DICT), b);
	}
	
	public Map<String, Map<String, List<ModelPropDict>>> getModelPropDict(){
		JedisUtil.Strings strings=jedisUtil.new Strings();
		byte[] b = strings.get(SerializeUtil.serialize(SYS_DICT));
		Map<String, Map<String, List<ModelPropDict>>> map = (Map<String, Map<String, List<ModelPropDict>>>)SerializeUtil.unserialize(b);
		return map;
	}
	
	
	public Map<String, List<ModelPropDict>> getDicts(String table) {
		if (StringUtils.isEmpty(table)) {
			return null;
		}
		return getModelPropDict().get(table.toUpperCase());
	}
	
	@Override
	public List<ModelPropDict> getDicts(String table, String field) {
		try {
			if (StringUtils.isEmpty(table) || StringUtils.isEmpty(field)) {
				return null;
			}
			return getModelPropDict().get(table.toUpperCase()).get(field.toUpperCase());
		} catch (Exception e) {
			ExceptUtils.printStackTrace(e, table + "." + field);
		}
		return new ArrayList<ModelPropDict>();
	}
	
	@Override
	public ModelPropDict getDict(String table, String field, String key) {
		List<ModelPropDict> list = getDicts(table, field);
		for (ModelPropDict dict : list) {
			if (dict.getKe().equals(key)) {
				return dict;
			}
		}
		return null;
	}
	
	@Override
	public List<ModelPropDict> getDictsByFields(String table,
			Collection<String> fields) {
		if (StringUtils.isEmpty(table) || fields == null || fields.size() == 0) {
			return null;
		}
		List<ModelPropDict> all = new ArrayList<ModelPropDict>();
		for (String field : fields) {
			List<ModelPropDict> childs = getDicts(table, field);
			if (childs == null) {
				logger.error("DICT LOAD FAIL -- {}.{}", table, field);
				continue;
			}
			all.addAll(childs);
		}
		return all;
	}
	
	
	private Map<String, Map<String, List<ModelPropDict>>> findAllSysDict(){
		Map<String, Map<String, List<ModelPropDict>>> map = new HashMap<String, Map<String, List<ModelPropDict>>>();
		List<ModelPropDict> list = modelPropdictRepo.findAll();
		for (ModelPropDict dict : list) {
			addDict(map, dict);
		}
		logger.info("加载数据字典条数：{}", list.size());
		return map;
	}
	
	private void addDict(Map<String, Map<String, List<ModelPropDict>>> dictMap, ModelPropDict dict) {
		try {
			if (StringUtils.isEmpty(dict.getClazz())) {
				return;
			}
			String table = dict.getClazz().toUpperCase();
			if (!dictMap.containsKey(table)) {
				dictMap.put(table, new HashMap<String, List<ModelPropDict>>());
			}
			if (StringUtils.isEmpty(dict.getField())) {
				return;
			}
			String filed = dict.getField().toUpperCase();

			if (!dictMap.get(table).containsKey(filed)) {
				dictMap.get(table).put(filed, new ArrayList<ModelPropDict>());
			}
			List<ModelPropDict> list = dictMap.get(table).get(filed);
			if (list.contains(dict)) {
				list.remove(dict);
			}
			list.add(dict);
		} catch (Exception e) {
			ExceptUtils.printStackTrace(e, "id[" + dict.getId() + "]");
		}
	}
	
	
}

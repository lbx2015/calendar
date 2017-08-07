package net.riking.service.repo.impl;

import java.util.ArrayList;
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
import net.riking.util.RedisUtil;

/***
 * 系统所有字典或常量数据接口
 * @author you.fei
 * @version crateTime：2017年8月5日 下午7:08:44
 * @used TODO
 */
@Service("sysDataService")
public class SysDataServiceImpl {
	Logger logger = LogManager.getLogger(getClass());
	
	@Autowired
	ModelPropdictRepo modelPropdictRepo;
	
	public void initData(){
		Map<String, Map<String, List<ModelPropDict>>> map = findAllSysDict();
		logger.info("initData=======================================================");
		RedisUtil redis = RedisUtil.getInstall();
		for(String key : map.keySet()){
			Map<String, List<ModelPropDict>> modelPropDictMap = map.get(key);
			redis.setMap(key, modelPropDictMap);
		}
		
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

package net.riking.service.impl;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import net.riking.core.entity.model.ModelPropDict;
import net.riking.core.service.repo.ModelPropdictRepo;
import net.riking.entity.model.CtryHdayCrcy;
import net.riking.service.SysDataService;
import net.riking.service.repo.CtryHdayCrcyRepo;
import net.riking.util.JedisUtil;
import net.riking.util.JedisUtil.Hash;
import net.riking.util.JedisUtil.Keys;

/***
 * 系统所有字典或常量数据接口实现
 * 
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
	CtryHdayCrcyRepo ctryHdayCrcyRepo;

	@Autowired
	JedisUtil jedisUtil;

	public static final String SYS_DICT = "DICT";

	public static final String CTRY_HDAY_CRCY = "CHC";
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public void initData() {
		logger.info("initData=======================================================");
		// 字典加载
		initDict();
		initCtryHdayCrcy();
	}

	@Override
	public List<ModelPropDict> getDicts(String table, String field) {
		if (StringUtils.isEmpty(table) || StringUtils.isEmpty(field)) {
			return null;
		}
		List<ModelPropDict> list = new ArrayList<ModelPropDict>();
		List<ModelPropDict> allDicts = getAllDicts();
		for (ModelPropDict dict : allDicts) {
			if(dict.getClazz().toUpperCase().equals(table) && dict.getField().toUpperCase().equals(field)){
				list.add(dict);
			}
		}
		return list;
	}
	
	@Override
	public ModelPropDict getDict(String table, String field, String key) {
		List<ModelPropDict> dicts_ = getDicts(table,field);
		for (ModelPropDict dict : dicts_) {
			if (dict.getKe().equals(key)) {
				return dict;
			}
		}
		return null;
	}
	
	@Override
	public List<ModelPropDict> getDictsByFields(String table, Collection<String> fields) {
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
	
	@Override
	public List<CtryHdayCrcy> getMoreCtryHdayCrcy(CtryHdayCrcy chc){
		List<CtryHdayCrcy> list = getAllCtryHdayCrcy();
		ArrayList<CtryHdayCrcy> list2 = new ArrayList<CtryHdayCrcy>();
		for (CtryHdayCrcy ctryHdayCrcy : list) {
			if(_equals(chc, ctryHdayCrcy)){
				list2.add(ctryHdayCrcy);
			}
		}
		return list2;
	}
	
	@Override
	public CtryHdayCrcy getCtryHdayCrcy(String id){
		List<CtryHdayCrcy> list = getAllCtryHdayCrcy();
		for (CtryHdayCrcy ctryHdayCrcy : list) {
			if(id.equals(ctryHdayCrcy.getId())){
				return ctryHdayCrcy;
			}
		}
		return null;
	}
	
	private void initDict(){
		List<ModelPropDict> list = modelPropdictRepo.findAll();
		Hash hash = jedisUtil.new Hash();
		for (ModelPropDict modelPropDict : list) {
			try {
				Map<String, String> map = obj2Map(modelPropDict);
				String key = SYS_DICT+":"+ modelPropDict.getId();
				hash.hmset(key, map);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void initCtryHdayCrcy(){
		CtryHdayCrcy ctryHdayCrcy = new CtryHdayCrcy();
		ctryHdayCrcy.setDeleteState("1");
		Example<CtryHdayCrcy> example = Example.of(ctryHdayCrcy, ExampleMatcher.matchingAll());
		List<CtryHdayCrcy> list = ctryHdayCrcyRepo.findAll(example);
		Hash hash = jedisUtil.new Hash();
		for (CtryHdayCrcy chc : list) {
			try {
				Map<String, String> map = obj2Map(chc);
				String key = CTRY_HDAY_CRCY+":"+ chc.getId();
				hash.hmset(key, map);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private List<ModelPropDict> getAllDicts() {
		List<ModelPropDict> list = new ArrayList<ModelPropDict>();
		Keys keys = jedisUtil.new Keys();
		Set<String> hkeys = keys.keys(SYS_DICT+":*");
		Hash hash = jedisUtil.new Hash();
		for (String hkey : hkeys) {
			Map<String, String> map = hash.hgetAll(hkey);
			ModelPropDict dict = new ModelPropDict();
			try {
				map2Obj(map, dict);
				list.add(dict);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	
	private List<CtryHdayCrcy> getAllCtryHdayCrcy(){
		List<CtryHdayCrcy> list = new ArrayList<CtryHdayCrcy>();
		Keys keys = jedisUtil.new Keys();
		Set<String> hkeys = keys.keys(CTRY_HDAY_CRCY+":*");
		Hash hash = jedisUtil.new Hash();
		for (String hkey : hkeys) {
			Map<String, String> map = hash.hgetAll(hkey);
			CtryHdayCrcy chc = new CtryHdayCrcy();
			try {
				map2Obj(map, chc);
				list.add(chc);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	private <T> Map<String, String> obj2Map(T t) throws IllegalArgumentException, IllegalAccessException{
		if (t == null){
			return null;
		}
		Class<? extends Object> clazz = t.getClass();
		Field[] fields = clazz.getDeclaredFields();
		Map<String,String> map = new HashMap<String,String>();
		for (int i = 0; i < fields.length; i++) {
			Field f = fields[i];
			f.setAccessible(true);
			Object value = f.get(t);
			if(f.getType()==String.class){
				map.put(f.getName(), value.toString());
			}else if(f.getType()==Long.class){
				map.put(f.getName(), value.toString());
			}else if(f.getType()==Date.class){
				map.put(f.getName(),sdf.format(value));
			}
		}
		return map;
	}
	
	private <T> void map2Obj(Map<String, String> map,T t) throws Exception{
		Class<? extends Object> clazz = t.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field f = fields[i];
			f.setAccessible(true);
			if(f.getType()==String.class){
				f.set(t, map.get(f.getName()));
			}else if(f.getType()==Long.class){
				f.set(t, Long.parseLong(map.get(f.getName())));
			}else if(f.getType()==Date.class){
				f.set(t, sdf.parse(map.get(f.getName())));
			}
		}
	}
	
	/**
	 * 
	 * @used TODO
	 * @param matchingObj 匹配参数对象
	 * @param obj 被匹配对象
	 * @return
	 */
	private <T> boolean _equals(T matchingObj,T obj){
		Field[] fields = matchingObj.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field f = fields[i];
			f.setAccessible(true);
			try {
				if(f.get(matchingObj)!=null && !f.get(matchingObj).equals(f.get(obj))){
					return false;
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
}

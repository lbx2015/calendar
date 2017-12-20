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

import net.riking.config.Const;
import net.riking.core.entity.model.ModelPropDict;
import net.riking.core.service.repo.ModelPropdictRepo;
import net.riking.core.utils.ExceptUtils;
import net.riking.dao.repo.SysDaysRepo;
import net.riking.entity.model.AppUser;
import net.riking.entity.model.SysDays;
import net.riking.service.ReportService;
import net.riking.service.SysDataService;
import net.riking.util.RedisUtil;

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
	ReportService reportService;
	
	/*@Autowired
	SysDaysRepo sysDaysRepo;*/

	// @Autowired
	// CtryHdayCrcyRepo ctryHdayCrcyRepo;

	/***
	 * 字典所有数据初始化
	 * @author zm.you
	 * @version crateTime：2017年8月9日 下午5:18:28
	 * @used TODO
	 */
	public void initData() {
		logger.info("====================================================initData=======================================================");
		initDict();
		//initSysDays();

		// initCtryHdayCrcy();
	}

	@SuppressWarnings("static-access")
	private void initDict() {
		Map<String, Map<String, List<ModelPropDict>>> map = findAllSysDict();
		/* tableName和field组合 */
		for (String tableName : map.keySet()) {
			Map<String, List<ModelPropDict>> clazzMap = map.get(tableName);
			for (String field : clazzMap.keySet()) {
				List<ModelPropDict> dictList = clazzMap.get(field);
				RedisUtil.getInstall().setList(tableName.toUpperCase() + ":" + field.toUpperCase(), dictList);

				for (ModelPropDict data : dictList) {
					/* tableName和field和key组合 */
					RedisUtil.getInstall().setObject(
							tableName.toUpperCase() + ":" + field.toUpperCase() + ":" + data.getKe().toUpperCase(),
							data);
				}
			}
		}
	}
	
	/*@SuppressWarnings("static-access")
	private void initSysDays() {
		List<SysDays> list = sysDaysRepo.findAll();
		for(SysDays day : list){
			RedisUtil.getInstall().setObject(Const.SYS_DAY + day.getDates()  + day.getDates(), day);
		}
	}*/

	/***
	 * 通过tableName和field获取字典
	 * @author zm.you
	 * @version crateTime：2017年8月9日 下午5:18:28
	 * @used TODO
	 */
	@SuppressWarnings("static-access")
	@Override
	public List<ModelPropDict> getDicts(String tableName, String field) {
		if (StringUtils.isBlank(tableName) || StringUtils.isBlank(field)) {
			return null;
		}
		List<ModelPropDict> list = RedisUtil.getInstall().getList(tableName.toUpperCase() + ":" + field.toUpperCase());
		return list;
	}

	/***
	 * 通过tableName和field和key获取某个字典对象
	 * @author zm.you
	 * @version crateTime：2017年8月9日 下午5:18:28
	 * @used TODO
	 */
	@SuppressWarnings("static-access")
	@Override
	public ModelPropDict getDict(String tableName, String field, String key) {
		if (StringUtils.isBlank(tableName) || StringUtils.isBlank(field) || StringUtils.isBlank(key)) {
			return null;
		}
		ModelPropDict dict = (ModelPropDict) RedisUtil.getInstall()
				.getObject(tableName.toUpperCase() + ":" + field.toUpperCase() + ":" + key.toUpperCase());
		return dict;
	}

	/***
	 * 通过tableName和指定的field获取字典列表
	 * @author zm.you
	 * @version crateTime：2017年8月9日 下午5:18:28
	 * @used TODO
	 */
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

	private Map<String, Map<String, List<ModelPropDict>>> findAllSysDict() {
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

	@SuppressWarnings("static-access")
	@Override
	public AppUser getAppUser(AppUser user) {
		AppUser appUser = (AppUser) RedisUtil.getInstall().getObject(user.getPhone());
		return appUser;
	}

	@SuppressWarnings("static-access")
	@Override
	public void setAppUser(AppUser user) {
		RedisUtil.getInstall().setObject(user.getPhone(), Const.VALID_CODE_TIME, user);
	}

	@SuppressWarnings("static-access")
	@Override
	public void delAppUser(AppUser user) {
		// TODO Auto-generated method stub
		// RedisUtil.getInstall().del(user.getPhoneDeviceid() + ":" + user.getPhone());
	}

	// @SuppressWarnings("static-access")
	// private void initCtryHdayCrcy(){
	// CtryHdayCrcy ctryHdayCrcy = new CtryHdayCrcy();
	// ctryHdayCrcy.setDeleteState("1");
	// Example<CtryHdayCrcy> example = Example.of(ctryHdayCrcy, ExampleMatcher.matchingAll());
	// List<CtryHdayCrcy> list = ctryHdayCrcyRepo.findAll(example);
	// logger.info("加载各国节假日条数：{}", list.size());
	// RedisUtil.getInstall().setList(Const.CTRY_HDAY_CRCY, list);
	//
	// for (CtryHdayCrcy chc : list) {
	// RedisUtil.getInstall().setObject(Const.CTRY_HDAY_CRCY+":"+chc.getId(), chc);
	// }
	// }

	// /**
	// *
	// * @used TODO
	// * @param matchingObj 匹配参数对象
	// * @param obj 被匹配对象
	// * @return
	// */
	// private <T> boolean matching(T matchingObj, T obj){
	// Field[] fields = matchingObj.getClass().getDeclaredFields();
	// for (int i = 0; i < fields.length; i++) {
	// Field f = fields[i];
	// f.setAccessible(true);
	// try {
	// if(f.get(matchingObj)!=null && !f.get(matchingObj).equals(f.get(obj))){
	// return false;
	// }
	// } catch (IllegalArgumentException | IllegalAccessException e) {
	// e.printStackTrace();
	// }
	// }
	// return true;
	// }

	// @SuppressWarnings("static-access")
	// @Override
	// public CtryHdayCrcy getCtryHdayCrcy(String id) {
	// CtryHdayCrcy chc =
	// (CtryHdayCrcy)RedisUtil.getInstall().getObject(Const.CTRY_HDAY_CRCY+":"+id);
	// return chc;
	// }
	//
	//
	// @SuppressWarnings("static-access")
	// @Override
	// public List<CtryHdayCrcy> getMoreCtryHdayCrcy(CtryHdayCrcy chc) {
	// List<CtryHdayCrcy> CtryHdayCrcyList = RedisUtil.getInstall().getList(Const.CTRY_HDAY_CRCY);
	// List<CtryHdayCrcy> list = new ArrayList<CtryHdayCrcy>();
	// for (CtryHdayCrcy ctryHdayCrcy : CtryHdayCrcyList) {
	// if(matching(chc, ctryHdayCrcy)){
	// list.addAll(CtryHdayCrcyList);
	// }
	// }
	// return list;
	// }
}

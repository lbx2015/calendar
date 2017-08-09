package net.riking.service;

import java.util.Collection;
import java.util.List;

import net.riking.core.entity.model.ModelPropDict;
/**
 * 
 * @author you.fei
 * @version crateTime：2017年8月8日 下午6:33:21
 * @used TODO
 * 系统所有字典或常量数据接口
 */
public interface SysDataService {
	/***
	 * 通过tableName和field获取字典
	 * @author zm.you
	 * @version crateTime：2017年8月9日 下午5:18:28
	 * @used TODO
	 */
	public List<ModelPropDict> getDicts(String table, String field);
	/***
	 * 通过tableName和field和key获取某个字典对象
	 * @author zm.you
	 * @version crateTime：2017年8月9日 下午5:18:28
	 * @used TODO
	 */
	public ModelPropDict getDict(String table, String field, String key);
	/***
	 * 通过tableName和指定的field获取字典列表
	 * @author zm.you
	 * @version crateTime：2017年8月9日 下午5:18:28
	 * @used TODO
	 */
	public List<ModelPropDict> getDictsByFields(String table,Collection<String> fields);
}

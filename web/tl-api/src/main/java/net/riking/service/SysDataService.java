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

	public List<ModelPropDict> getDicts(String table, String field);
	
	public ModelPropDict getDict(String table, String field, String key);
	
	public List<ModelPropDict> getDictsByFields(String table,Collection<String> fields);
}

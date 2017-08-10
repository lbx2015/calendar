package net.riking.service;

import java.util.Collection;
import java.util.List;

import net.riking.core.entity.model.ModelPropDict;
public interface SysDataService {

	public List<ModelPropDict> getDicts(String table, String field);
	
	public ModelPropDict getDict(String table, String field, String key);
	
	public List<ModelPropDict> getDictsByFields(String table,Collection<String> fields);
	
//	public CtryHdayCrcy getCtryHdayCrcy(String id);
//	
//	public List<CtryHdayCrcy> getMoreCtryHdayCrcy(CtryHdayCrcy chc);
	
}

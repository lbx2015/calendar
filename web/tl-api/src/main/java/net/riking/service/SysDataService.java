package net.riking.service;

import java.util.Collection;
import java.util.List;

import net.riking.core.entity.model.ModelPropDict;
import net.riking.entity.model.AppUser;
import net.riking.entity.model.AppUserGrade;
import net.riking.entity.model.EmailSuffix;

public interface SysDataService {

	public List<ModelPropDict> getDicts(String table, String field);

	public ModelPropDict getDict(String table, String field, String key);

	public List<ModelPropDict> getDictsByFields(String table, Collection<String> fields);

	// public CtryHdayCrcy getCtryHdayCrcy(String id);
	//
	// public List<CtryHdayCrcy> getMoreCtryHdayCrcy(CtryHdayCrcy chc);

	public AppUser getAppUser(AppUser user);

	public void setAppUser(AppUser user);

	public void delAppUser(AppUser user);

	public List<AppUserGrade> getGrade(String key);

	public List<EmailSuffix> getEmailSuffix(String key);

}

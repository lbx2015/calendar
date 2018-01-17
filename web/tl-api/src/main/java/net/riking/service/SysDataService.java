package net.riking.service;

import java.util.Collection;
import java.util.List;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;

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

//	public void delAppUser(AppUser user);

	public List<AppUserGrade> getGrade(String key);

	public List<EmailSuffix> getEmailSuffix(String key);

	/***
	 * 根据邮件主题，创建邮件
	 * @author james.you
	 * @version crateTime：2018年1月12日 下午7:21:37
	 * @used TODO
	 * @param theme 主题
	 * @return
	 * @throws EmailException
	 */
	Email getEmail(String theme) throws EmailException;

}

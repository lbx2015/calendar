package net.riking.entity.VO;

import net.riking.entity.model.AppUser;
import net.riking.entity.model.AppUserDetail;

/**
 * WEB端：appUser VO对象
 * 
 * @author fu.chen
 *
 */

public class AppUserVO {
	private String id;

	private AppUser appUser;

	private AppUserDetail appUserDetail;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public AppUser getAppUser() {
		return appUser;
	}

	public void setAppUser(AppUser appUser) {
		this.appUser = appUser;
	}

	public AppUserDetail getAppUserDetail() {
		return appUserDetail;
	}

	public void setAppUserDetail(AppUserDetail appUserDetail) {
		this.appUserDetail = appUserDetail;
	}

}

package net.riking.entity.params;

import java.io.Serializable;

/**
 * App版本获取接口参数
 * @author james.you
 * @version crateTime：2017年11月28日 下午2:39:26
 * @used TODO
 */
public class ValidParams implements Serializable {
	private static final long serialVersionUID = 1L;

	// 手机号
	private String phone;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
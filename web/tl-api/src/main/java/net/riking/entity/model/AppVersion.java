package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import net.riking.core.annos.Comment;
import net.riking.entity.BaseProp;

/**
 * 
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
@Comment("APP版本表")
@Entity
@Table(name = "t_app_version")
public class AppVersion extends BaseProp {
	@Id
	@Column(name = "Id", length = 32)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;

	@Comment("app版本号")
	@Column(name = "version_no", length = 32)
	private String versionNo;

	@Comment("是否强制更新(0-不强制；1-强制)")
	@Column(name = "force")
	private Integer force;

	// 版本说明
	@Column(name = "remark", length = 255)
	private String remark;

	// // 更新日期
	// @Column(name = "renewal_time", length = 8)
	// private String renewalTime;
	//
	// // 客户端类型：1-IOS;2-Android;3-其它
	// @Column(name = "type", length = 1)
	// private String type;
	//
	// // apk路径
	// @Column(name = "apk_url", length = 128)
	// private String apkUrl;
	//
	// // 删除状态 0删除 1显示
	// @Column(name = "delete_state", length = 1)
	// private String deleteState;
	//
	// @Column(name = "forces", length = 2)
	// private String forces;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}

	public Integer getForce() {
		return force;
	}

	public void setForce(Integer force) {
		this.force = force;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}

package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonProperty;

import net.riking.core.annos.Comment;
import net.riking.entity.BaseProp;

/**
 * 
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
@Comment("Web版本")
@Entity
@Table(name = "t_web_version")
public class WebVersion extends BaseProp {

	/**
	 * 
	 */
	private static final long serialVersionUID = -777861709889682962L;

	/**
	 * @author Lucky.Liu on 2017/8/05.
	 */

	@Id
	@Column(name = "Id", length = 32)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	@JsonProperty("webVersionId")
	private String id;

	@Comment("WEB版本号(如V1.0.0）")
	@Column(name = "version_no", length = 32)
	private String versionNo;

	@Comment("数据库脚本版本号")
	@Column(name = "sql_version_no", length = 32)
	private String sqlVersionNo;

	// 版本说明
	@Comment("版本说明")
	@Column(name = "remark", length = 255)
	private String remark;

	@Comment("操作类型：modify-修改;add-新增")
	@Transient
	private String opt;

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

	public String getSqlVersionNo() {
		return sqlVersionNo;
	}

	public void setSqlVersionNo(String sqlVersionNo) {
		this.sqlVersionNo = sqlVersionNo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	// // 更新日期
	// @Column(name = "renewal_time", length = 8)
	// private String renewalTime;
	//
	// // 删除状态 0删除 1显示
	// @Column(name = "delete_state", length = 2)
	// private String deleteState;
	public String getOpt() {
		return opt;
	}

	public void setOpt(String opt) {
		this.opt = opt;
	}
}

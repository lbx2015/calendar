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
@Comment("APP版本表")
@Entity
@Table(name = "t_app_version")
public class AppVersion extends BaseProp {
	private static final long serialVersionUID = 1L;

	@Comment("物理主键")
	@Id
	@Column(name = "id", length = 32)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	@JsonProperty("appVersionId")
	private String id;

	@Comment("app版本号")
	@Column(name = "version_no", length = 32)
	private String versionNo;

	@Comment("移动端：1-IOS；2-Android")
	@Column(name = "client_type", length = 1)
	private Integer clientType;

	@Comment("是否强制更新(0-不强制；1-强制)")
	@Column(name = "enforce")
	private Integer enforce;

	@Comment("用户状态 0-禁用 1-启用")
	@org.hibernate.annotations.ColumnDefault("0")
	@Column(name = "enabled", nullable = false, precision = 1)
	private Integer enabled;

	@Comment("下载更新地址")
	@Column(name = "url", length = 255)
	private String url;

	@Comment("备注")
	@Column(name = "remark", length = 255)
	private String remark;

	@Comment("操作类型：modify-修改;add-新增")
	@Transient
	private String opt;

	public AppVersion() {
	}

	public AppVersion(String versionNo, Integer enforce, String url, String remark) {
		super();
		this.versionNo = versionNo;
		this.enforce = enforce;
		this.url = url;
		this.remark = remark;
	}

	public String getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getClientType() {
		return clientType;
	}

	public void setClientType(Integer clientType) {
		this.clientType = clientType;
	}

	public Integer getEnforce() {
		return enforce;
	}

	public void setEnforce(Integer enforce) {
		this.enforce = enforce;
	}

	public Integer getEnabled() {
		return enabled;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getOpt() {
		return opt;
	}

	public void setOpt(String opt) {
		this.opt = opt;
	}

}

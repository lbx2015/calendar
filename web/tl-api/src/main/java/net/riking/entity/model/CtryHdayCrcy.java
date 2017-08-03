package net.riking.entity.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import net.riking.core.annos.Comment;
import net.riking.core.entity.PageQuery;
import net.riking.entity.BaseEntity;

@Entity
@Table(name = "t_ctry_hday_crcy")
public class CtryHdayCrcy extends PageQuery {
//各国节假日币种表
	@Id
	@Column(name = "Id", length = 32)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;
	
	@Comment("国旗图片url")
	@Column(name = "icon", length = 64)
	private String icon;
	
	@Comment("国家/地区名称")
	@Column(name = "ctry_name", length = 32)
	private String ctryName;
	
	@Comment("节假日名称")
	@Column(name = "hday_name", length = 32)
	private String hdayName;
	
	@Comment("节假日时间")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "hday_date")
	private Date hdayDate;
	
	@Comment("币种")
	@Column(name = "crcy", length = 3)
	private String crcy;
	
	@Comment("备注信息")
	@Column(name = "remark", length = 500)
	private String remark;
	
	//0-删除状态   1-未删除状态
	@Comment("删除标记")
	@Column(name = "delete_state")
	private String deleteState;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getCtryName() {
		return ctryName;
	}

	public void setCtryName(String ctryName) {
		this.ctryName = ctryName;
	}

	public String getHdayName() {
		return hdayName;
	}

	public void setHdayName(String hdayName) {
		this.hdayName = hdayName;
	}

	public Date getHdayDate() {
		return hdayDate;
	}

	public void setHdayDate(Date hdayDate) {
		this.hdayDate = hdayDate;
	}

	public String getCrcy() {
		return crcy;
	}

	public void setCrcy(String crcy) {
		this.crcy = crcy;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDeleteState() {
		return deleteState;
	}

	public void setDeleteState(String deleteState) {
		this.deleteState = deleteState;
	}
	
	
	
}

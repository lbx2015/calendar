package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import net.riking.core.annos.Comment;
import net.riking.core.entity.PageQuery;
import net.riking.service.getDateService;

@Entity
@Table(name = "t_ctry_hday_crcy")
public class CtryHdayCrcy extends PageQuery {

	//各国节假日币种表
	@Id
	@Column(name = "Id", length = 32)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;
	
	
	@Comment("国家/地区名称")
	@Column(name = "ctry_name", length = 32)
	private String ctryName;
	
	@Transient
	private String ctryNameValue;
	
	@Comment("节假日名称")
	@Column(name = "hday_name", length = 32)
	private String hdayName;
	
	@Transient
	private String hdayNameValue;
	
	@Comment("节假日时间")
	@Column(name = "hday_date", length = 8)
	private String hdayDate;
	
	@Comment("币种")
	@Column(name = "crcy", length = 3)
	private String crcy;
	
//	@Transient
//	private String crcyValue;
	
	@Comment("备注信息")
	@Column(name = "remark", length = 500)
	private String remark;
	
	//0-删除状态   1-未删除状态
	@Comment("删除标记")
	@Column(name = "delete_state",length = 1)
	private String deleteState;
	
	@Transient
	private String queryParam;
	
	@Transient
	private String iconUrl;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getHdayDate() {
		return hdayDate;
	}

	public void setHdayDate(String hdayDate) {
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

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getCtryNameValue() {
		return ctryNameValue;
	}

	public void setCtryNameValue(String ctryNameValue) {
		this.ctryNameValue = ctryNameValue;
	}

	public String getHdayNameValue() {
		return hdayNameValue;
	}

	public void setHdayNameValue(String hdayNameValue) {
		this.hdayNameValue = hdayNameValue;
	}

	public String getQueryParam() {
		return queryParam;
	}

	public void setQueryParam(String queryParam) {
		this.queryParam = queryParam;
	}

//	public String getCrcyValue() {
//		return crcyValue;
//	}
//
//	public void setCrcyValue(String crcyValue) {
//		this.crcyValue = crcyValue;
//	}
//	
	
	public Integer getPcount() {
		Integer pcount = super.getPcount();
		if(pcount==null||pcount<10){
			return 10;
		}
		return pcount;
	}
	
	
}

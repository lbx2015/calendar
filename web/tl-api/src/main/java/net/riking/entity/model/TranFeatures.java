package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import net.riking.core.annos.Comment;


/**
 * Created by bing.xun on 2017/5/24.
 */
@Entity
@Table(name = "T_AML_TRAN_FEATURES")
public class TranFeatures {
    @Id
    @GeneratedValue
    private Long id;
    
    /**特征代码 */
    private String tzdm;
    
    /**特征说明 */
    private String tzsm;
    
    /**涉罪类型代码（36种） */
    private String szlxdm;
    
    /**监测标准（技术指标）（已审核的选择）*/
    private String jcbz;
    
    /**特征产生方法 */
    private String tzcsff;
    
    /**其他说明 */
    private String qtsm;
    
    /**阈值分数 */
    private Integer fzfs;
    
    /**类型（可疑或大额） */
    private String type;
    
    //黑名单匹配属性
    @Column(name="black_props",length = 100)
    private String blackProps;

    /** 名单类型 */
    @Comment("黑名单类型")
    @Column(name="hmdlx",length = 10)
    private String hmdlx;

    /** 名单来源 */
    @Comment("黑名单来源")
    @Column(name="mdly",length = 100)
    private String mdly;
    
    /** 审核状态1-审核通过，0-审核不通过 */
    @Column(name = "approved", length = 1)
    private Integer approved;
    
    

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTzdm() {
		return tzdm;
	}

	public void setTzdm(String tzdm) {
		this.tzdm = tzdm;
	}

	public String getTzsm() {
		return tzsm;
	}

	public void setTzsm(String tzsm) {
		this.tzsm = tzsm;
	}

	public String getSzlxdm() {
		return szlxdm;
	}

	public void setSzlxdm(String szlxdm) {
		this.szlxdm = szlxdm;
	}

	public String getJcbz() {
		return jcbz;
	}

	public void setJcbz(String jcbz) {
		this.jcbz = jcbz;
	}

	public String getTzcsff() {
		return tzcsff;
	}

	public void setTzcsff(String tzcsff) {
		this.tzcsff = tzcsff;
	}

	public String getQtsm() {
		return qtsm;
	}

	public void setQtsm(String qtsm) {
		this.qtsm = qtsm;
	}

	public Integer getFzfs() {
		return fzfs;
	}

	public void setFzfs(Integer fzfs) {
		this.fzfs = fzfs;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBlackProps() {
		return blackProps;
	}

	public void setBlackProps(String blackProps) {
		this.blackProps = blackProps;
	}

	public String getHmdlx() {
		return hmdlx;
	}

	public void setHmdlx(String hmdlx) {
		this.hmdlx = hmdlx;
	}

	public String getMdly() {
		return mdly;
	}

	public void setMdly(String mdly) {
		this.mdly = mdly;
	}
	
	public String getSzlxdmKey(){
		return szlxdm;
	}

	public Integer getApproved() {
		return approved;
	}

	public void setApproved(Integer approved) {
		this.approved = approved;
	}

	
}

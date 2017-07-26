package net.riking.entity.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import net.riking.core.entity.model.Job;

import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;
import net.riking.core.annos.Comment;
import net.riking.core.entity.ILog;
import net.riking.core.entity.LogConfig;
import net.riking.core.entity.model.BaseFlowJob;

/**
 * Created by bing.xun on 2017/6/5.
 */
@Comment("大额")
@Entity
@Table(name = "T_AML_BigAmount")
public class BigAmount extends BaseFlowJob implements ILog {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public BigAmount() {
	}

	public BigAmount(String crtp, BigDecimal crat, String tsdr) {
		this.crtp = crtp;
		this.crat = crat;
		this.tsdr = tsdr;
	}

	public BigAmount(String crtp, BigDecimal crat, String tsdr, String ctid, String crcd) {
		this.crtp = crtp;
		this.crat = crat;
		this.tsdr = tsdr;
		this.ctid = ctid;
		this.crcd = crcd;
	}

	public BigAmount(Long id, String curJobState) {
		this.id = id;
		this.job = new Job();
		this.job.setCurJobState(curJobState);
	}

	/** 系统ID */
	@Comment("系统ID")
	@Id
	@Column(name = "id")
	@GeneratedValue
	private Long id;

	/** 报告机构编码 */
	@Comment("报告机构编码")
	@Column(name = "ricd", length = 16)
	private String ricd;

	/** 网点代码 */
	@Comment("网点代码")
	@Column(name = "finc", length = 16)
	private String finc;

	/** 金融机构与客户的关系 */
	@Comment("金融机构与客户的关系")
	@Column(name = "rlfc", length = 2)
	private String rlfc;

	/** 大额交易发生日期 */
	@Temporal(TemporalType.DATE)
	@Comment("大额交易发生日")
	@Column(name = "htdt", length = 8)
	private Date htdt;

	/** 大额交易特征代码 */
	@Comment("大额交易特征代码")
	@Column(name = "crcd", length = 4)
	private String crcd;

	/** 客户号 */
	@Comment("客户号")
	@Column(name = "csnm", length = 32)
	private String csnm;

	/** 客户身份证件/证明文件类型 */
	@Comment("客户身份证件/证明文件类型")
	@Column(name = "citp", length = 6)
	private String citp;

	/** 其他身份证件/证明文件类型（客户其他证件类型说明） */
	@Comment("其他身份证件/证明文件类型（客户其他证件类型说明）")
	@Column(name = "oitp1", length = 32)
	private String oitp1;

	/** 客户身份证件/证明文件号码 */
	@Comment("客户身份证件/证明文件号码")
	@Column(name = "ctid", length = 128)
	private String ctid;

	/** 客户国籍 */
	@Comment(" 客户国籍")
	@Column(name = "ctnt", length = 30)
	private String ctnt;

	/** 客户职业（对私）或客户行业（对公） */
	@Comment("客户职业（对私）或客户行业（对公）")
	@Column(name = "ctvc", length = 32)
	private String ctvc;

	/** 客户联系电话 */
	@Comment("客户联系电话")
	@Column(name = "cctl", length = 64)
	private String cctl;

	/** 客户其他联系方式 */
	@Comment("客户其他联系方式")
	@Column(name = "ccei", length = 512)
	private String ccei;

	/** 客户住址/经营地址 */
	@Comment("客户住址/经营地址")
	@Column(name = "ctar", length = 512)
	private String ctar;

	/** 客户姓名/名称 */
	@Comment("客户姓名/名称")
	@Column(name = "ctnm", length = 512)
	private String ctnm;

	/** 客户账户类型 */
	@Comment("客户账户类型")
	@Column(name = "catp", length = 6)
	private String catp;

	/** 客户账号 */
	@Comment("客户账号")
	@Column(name = "ctac", length = 64)
	private String ctac;

	/** 客户账户开立时间 */
	@Comment(" 客户账户开立时间")
	@Temporal(TemporalType.DATE)
	@Column(name = "oatm", length = 14)
	private Date oatm;

	/** 客户银行卡类型 */
	@Comment("客户银行卡类型")
	@Column(name = "cbct", length = 2)
	private String cbct;

	/** 客户银行卡其他类型 */
	@Comment("客户银行卡其他类型")
	@Column(name = "ocbt", length = 32)
	private String ocbt;

	/** 客户银行卡号码 */
	@Comment("客户银行卡号码")
	@Column(name = "cbcn", length = 64)
	private String cbcn;

	/** 交易代办人姓名 */
	@Comment("交易代办人姓名")
	@Column(name = "tbnm", length = 128)
	private String tbnm;

	/** 交易代办人身份证件/证明文件类型 */
	@Comment("交易代办人身份证件/证明文件类型")
	@Column(name = "tbit", length = 6)
	private String tbit;

	/** 其他身份证件/证明文件类型（代办人其他证件类型说明） */
	@Comment("其他身份证件/证明文件类型（代办人其他证件类型说明）")
	@Column(name = "oitp2", length = 32)
	private String oitp2;

	/** 交易代办人身份证件/证明文件号码 */
	@Comment("交易代办人身份证件/证明文件号码")
	@Column(name = "tbid", length = 128)
	private String tbid;

	/** 交易代办人国籍 */
	@Comment("交易代办人国籍")
	@Column(name = "tbnt", length = 3)
	private String tbnt;

	/** 交易时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	@Comment("交易时间")
	@Column(name = "tstm", length = 14)
	private Date tstm;

	/** 交易发生地 */
	@Comment("交易发生地")
	@Column(name = "trcd", length = 9)
	private String trcd;

	/** 业务标识号 */
	@Comment("业务标识号")
	@Column(name = "ticd", length = 256)
	private String ticd;

	/** 收付款方匹配号类型 */
	@Comment("收付款方匹配号类型")
	@Column(name = "rpmt", length = 2)
	private String rpmt;

	/** 收付款方匹配号 */
	@Comment("收付款方匹配号")
	@Column(name = "rpmn", length = 500)
	private String rpmn;

	/** 交易方式 */
	@Comment("交易方式")
	@Column(name = "tstp", length = 6)
	private String tstp;

	/** 非柜台交易方式 */
	@Comment("非柜台交易方式")
	@Column(name = "octt", length = 2)
	private String octt;

	/** 其他非柜台交易方式 */
	@Comment("其他非柜台交易方式")
	@Column(name = "ooct", length = 32)
	private String ooct;

	/** 非柜台交易方式的设备代码 */
	@Comment("非柜台交易方式的设备代码")
	@Column(name = "ocec", length = 500)
	private String ocec;

	/** 银行与支付机构之间的业务交易编码 */
	@Comment("银行与支付机构之间的业务交易编码")
	@Column(name = "bptc", length = 500)
	private String bptc;

	/** 涉外收支交易分类与代码 */
	@Comment("涉外收支交易分类与代码")
	@Column(name = "tsct", length = 16)
	private String tsct;

	/** 资金收付标志 */
	@Comment("资金收付标志")
	@Column(name = "tsdr", length = 2)
	private String tsdr;

	/** 资金用途 */
	@Comment(" 资金用途")
	@Column(name = "crpp", length = 128)
	private String crpp;

	/** 交易币种 */
	@Comment("交易币种")
	@Column(name = "crtp", length = 3)
	private String crtp;

	/** 交易金额 */
	@Comment("交易金额")
	@Column(name = "crat", length = 20)
	private BigDecimal crat;

	/** 交易金额（折人民币） */
	@Comment("交易金额（折人民币）")
	@Column(name = "crmb", length = 20)
	private BigDecimal crmb;

	/** 交易金额（折美元） */
	@Comment("交易金额（折美元）")
	@Column(name = "cusd", length = 20)
	private BigDecimal cusd;

	/** 对方金融机构网点名称 */
	@Comment("对方金融机构网点名称")
	@Column(name = "cfin", length = 64)
	private String cfin;

	/** 对方金融机构网点代码类型 */
	@Comment("对方金融机构网点代码类型")
	@Column(name = "cfct", length = 2)
	private String cfct;

	/** 对方金融机构网点代码 */
	@Comment("对方金融机构网点代码")
	@Column(name = "cfic", length = 16)
	private String cfic;

	/** 对方金融机构网点行政区划代码 */
	@Comment("对方金融机构网点行政区划代码")
	@Column(name = "cfrc", length = 9)
	private String cfrc;

	/** 交易对手姓名/名称 */
	@Comment("交易对手姓名/名称")
	@Column(name = "tcnm", length = 128)
	private String tcnm;

	/** 交易对手身份证件/证明文件类型 */
	@Comment("交易对手身份证件/证明文件类型")
	@Column(name = "tcit", length = 6)
	private String tcit;

	/** 其他身份证件/证明文件类型（交易对手其他证件类型说明） */
	@Comment("其他身份证件/证明文件类型（交易对手其他证件类型说明）")
	@Column(name = "oitp3", length = 32)
	private String oitp3;

	/** 交易对手身份证件/证明文件号码 */
	@Comment("交易对手身份证件/证明文件号码")
	@Column(name = "tcid", length = 128)
	private String tcid;

	/** 交易对手账户类型 */
	@Comment("交易对手账户类型")
	@Column(name = "tcat", length = 6)
	private String tcat;

	/** 交易对手账号 */
	@Comment("交易对手账号")
	@Column(name = "tcac", length = 64)
	private String tcac;

	/** 交易信息备注1 */
	@Comment("交易信息备注1")
	@Column(name = "rotf1", length = 64)
	private String rotf1;

	/** 交易信息备注2 */
	@Comment("交易信息备注2")
	@Column(name = "rotf2", length = 64)
	private String rotf2;

	/** 人工补正标识 */
	@Comment("人工补正标识")
	@Column(name = "mirs", length = 64)
	private String mirs;

	/** 定位信息 */
	@Comment("定位信息")
	@Column(name = "dwxx", length = 128)
	private String dwxx;

	/** 机构编码 **/
	@Comment("机构编码")
	@Column(name = "jgbm", length = 32)
	private String jgbm;

	/** 查询上报状态 */
	@Comment("查询上报状态")
	@Column(name = "upState", length = 128)
	private String upState;

	/** 查询时间类型 */
	@Comment("查询时间类型")
	@Transient
	private String queryDate;

	/** 查询开始时间 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Comment("查询开始时间")
	@Transient
	private Date starts;

	/** 查询结束时间 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Comment("查询结束时间")
	@Transient
	private Date ends;

	/** 是否审核不通过标记 */
	@Comment("是否审核不通过标记")
	@Column(name = "aptp")
	private String aptp;

	/** 是否满足报送状态 */
	@Comment("是否满足报送状态")
	@Column(name = "sfmz", length = 20)
	private String sfmz;

	/** 汇总金额 */
	@Comment("汇总金额")
	private String hzje;

	/** 删除状态字段1显示2删除 */
	@Comment("删除状态字段1显示2删除")
	@Column(name = "deleteState", length = 20)
	private String deleteState;

	/** 检验状态 */
	@Comment("检验状态")
	@Column(name = "checkType", length = 20)
	private String checkType;

	/** 现报送批次 */
	@Comment("现报送批次")
	@Column(name = "nowsubmitBatch")
	private String nowSubmitBatch;

	/** 报送类型 */
	@Comment("报送类型")
	@Column(name = "reportType")
	private String reportType;

	/** 报文状态 */
	@Comment("报文状态")
	@Column(name = "bwzt")
	private String bwzt;

	/** 采集时间 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Comment("采集时间")
	@Temporal(TemporalType.DATE)
	@Column(name = "rpdt")
	private Date rpdt;

	/** 报送批次 */
	@Comment("报送批次")
	@Column(name = "submitBatch")
	private String submitBatch;

	/** 原报送日期 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Comment("原报送时间")
	@Temporal(TemporalType.DATE)
	@Column(name = "orpdt")
	private Date orpdt;

	/** 报文名称 */
	@Comment("报文名称")
	@Column(name = "tname")
	private String tname;

	/** 上报状态 */
	@Comment("上报状态")
	@Column(name = "submitType", length = 20)
	private String submitType;

	/** 不上报备注 */
	@Comment("不上报备注")
	@Column(name = "remarks", length = 400)
	private String remarks;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRicd() {
		return ricd;
	}

	public void setRicd(String ricd) {
		this.ricd = ricd;
	}

	public String getFinc() {
		return finc;
	}

	public void setFinc(String finc) {
		this.finc = finc;
	}

	public String getRlfc() {
		return rlfc;
	}

	public void setRlfc(String rlfc) {
		this.rlfc = rlfc;
	}

	public Date getHtdt() {
		return htdt;
	}

	public void setHtdt(Date htdt) {
		this.htdt = htdt;
	}

	public String getCrcd() {
		return crcd;
	}

	public void setCrcd(String crcd) {
		this.crcd = crcd;
	}

	public String getCsnm() {
		return csnm;
	}

	public void setCsnm(String csnm) {
		this.csnm = csnm;
	}

	public String getCitp() {
		return citp;
	}

	public void setCitp(String citp) {
		this.citp = citp;
	}

	public String getOitp1() {
		return oitp1;
	}

	public void setOitp1(String oitp1) {
		this.oitp1 = oitp1;
	}

	public String getCtid() {
		return ctid;
	}

	public void setCtid(String ctid) {
		this.ctid = ctid;
	}

	public String getCtnt() {
		return ctnt;
	}

	public void setCtnt(String ctnt) {
		this.ctnt = ctnt;
	}

	public String getCtvc() {
		return ctvc;
	}

	public void setCtvc(String ctvc) {
		this.ctvc = ctvc;
	}

	public String getCctl() {
		return cctl;
	}

	public void setCctl(String cctl) {
		this.cctl = cctl;
	}

	public String getCcei() {
		return ccei;
	}

	public void setCcei(String ccei) {
		this.ccei = ccei;
	}

	public String getCtar() {
		return ctar;
	}

	public void setCtar(String ctar) {
		this.ctar = ctar;
	}

	public String getCtnm() {
		return ctnm;
	}

	public void setCtnm(String ctnm) {
		this.ctnm = ctnm;
	}

	public String getCatp() {
		return catp;
	}

	public void setCatp(String catp) {
		this.catp = catp;
	}

	public String getCtac() {
		return ctac;
	}

	public void setCtac(String ctac) {
		this.ctac = ctac;
	}

	public Date getOatm() {
		return oatm;
	}

	public void setOatm(Date oatm) {
		this.oatm = oatm;
	}

	public String getCbct() {
		return cbct;
	}

	public void setCbct(String cbct) {
		this.cbct = cbct;
	}

	public String getOcbt() {
		return ocbt;
	}

	public void setOcbt(String ocbt) {
		this.ocbt = ocbt;
	}

	public String getCbcn() {
		return cbcn;
	}

	public void setCbcn(String cbcn) {
		this.cbcn = cbcn;
	}

	public String getTbnm() {
		return tbnm;
	}

	public void setTbnm(String tbnm) {
		this.tbnm = tbnm;
	}

	public String getTbit() {
		return tbit;
	}

	public void setTbit(String tbit) {
		this.tbit = tbit;
	}

	public String getOitp2() {
		return oitp2;
	}

	public void setOitp2(String oitp2) {
		this.oitp2 = oitp2;
	}

	public String getTbid() {
		return tbid;
	}

	public void setTbid(String tbid) {
		this.tbid = tbid;
	}

	public String getTbnt() {
		return tbnt;
	}

	public void setTbnt(String tbnt) {
		this.tbnt = tbnt;
	}

	public Date getTstm() {
		return tstm;
	}

	public void setTstm(Date tstm) {
		this.tstm = tstm;
	}

	public String getTrcd() {
		return trcd;
	}

	public void setTrcd(String trcd) {
		this.trcd = trcd;
	}

	public String getTicd() {
		return ticd;
	}

	public void setTicd(String ticd) {
		this.ticd = ticd;
	}

	public String getRpmt() {
		return rpmt;
	}

	public void setRpmt(String rpmt) {
		this.rpmt = rpmt;
	}

	public String getRpmn() {
		return rpmn;
	}

	public void setRpmn(String rpmn) {
		this.rpmn = rpmn;
	}

	public String getTstp() {
		return tstp;
	}
	//真实值
	public String getTstp2(){
		if(StringUtils.isNotEmpty(tstp)&&tstp.contains("_")){
			return tstp.substring(tstp.indexOf("_")+1);
		}else{
			return tstp;
		}
	}

	public void setTstp(String tstp) {
		this.tstp = tstp;
	}

	public String getOctt() {
		return octt;
	}

	public void setOctt(String octt) {
		this.octt = octt;
	}

	public String getOoct() {
		return ooct;
	}

	public void setOoct(String ooct) {
		this.ooct = ooct;
	}

	public String getOcec() {
		return ocec;
	}

	public void setOcec(String ocec) {
		this.ocec = ocec;
	}

	public String getBptc() {
		return bptc;
	}

	public void setBptc(String bptc) {
		this.bptc = bptc;
	}

	public String getTsct() {
		return tsct;
	}

	public void setTsct(String tsct) {
		this.tsct = tsct;
	}

	public String getTsdr() {
		return tsdr;
	}

	public void setTsdr(String tsdr) {
		this.tsdr = tsdr;
	}

	public String getCrpp() {
		return crpp;
	}

	public void setCrpp(String crpp) {
		this.crpp = crpp;
	}

	public String getCrtp() {
		return crtp;
	}

	public String getCrtpCode() {
		return crtp;
	}

	public void setCrtp(String crtp) {
		this.crtp = crtp;
	}

	public BigDecimal getCrat() {
		return crat;
	}

	public void setCrat(BigDecimal crat) {
		this.crat = crat;
	}

	public BigDecimal getCrmb() {
		return crmb;
	}

	public void setCrmb(BigDecimal crmb) {
		this.crmb = crmb;
	}

	public BigDecimal getCusd() {
		return cusd;
	}

	public void setCusd(BigDecimal cusd) {
		this.cusd = cusd;
	}

	public String getCfin() {
		return cfin;
	}

	public void setCfin(String cfin) {
		this.cfin = cfin;
	}

	public String getCfct() {
		return cfct;
	}

	public void setCfct(String cfct) {
		this.cfct = cfct;
	}

	public String getCfic() {
		return cfic;
	}

	public void setCfic(String cfic) {
		this.cfic = cfic;
	}

	public String getCfrc() {
		return cfrc;
	}

	public void setCfrc(String cfrc) {
		this.cfrc = cfrc;
	}

	public String getTcnm() {
		return tcnm;
	}

	public void setTcnm(String tcnm) {
		this.tcnm = tcnm;
	}

	public String getTcit() {
		return tcit;
	}

	public void setTcit(String tcit) {
		this.tcit = tcit;
	}

	public String getOitp3() {
		return oitp3;
	}

	public void setOitp3(String oitp3) {
		this.oitp3 = oitp3;
	}

	public String getTcid() {
		return tcid;
	}

	public void setTcid(String tcid) {
		this.tcid = tcid;
	}

	public String getTcat() {
		return tcat;
	}

	public void setTcat(String tcat) {
		this.tcat = tcat;
	}

	public String getTcac() {
		return tcac;
	}

	public void setTcac(String tcac) {
		this.tcac = tcac;
	}

	public String getRotf1() {
		return rotf1;
	}

	public void setRotf1(String rotf1) {
		this.rotf1 = rotf1;
	}

	public String getRotf2() {
		return rotf2;
	}

	public void setRotf2(String rotf2) {
		this.rotf2 = rotf2;
	}

	public String getMirs() {
		return mirs;
	}

	public void setMirs(String mirs) {
		this.mirs = mirs;
	}

	public String getDwxx() {
		return dwxx;
	}

	public void setDwxx(String dwxx) {
		this.dwxx = dwxx;
	}

	public String getJgbm() {
		return jgbm;
	}

	public void setJgbm(String jgbm) {
		this.jgbm = jgbm;
	}

	public String getUpState() {
		return upState;
	}

	public void setUpState(String upState) {
		this.upState = upState;
	}

	public String getQueryDate() {
		return queryDate;
	}

	public void setQueryDate(String queryDate) {
		this.queryDate = queryDate;
	}

	public Date getStarts() {
		return starts;
	}

	public void setStarts(Date starts) {
		this.starts = starts;
	}

	public Date getEnds() {
		return ends;
	}

	public void setEnds(Date ends) {
		this.ends = ends;
	}

	public String getAptp() {
		return aptp;
	}

	public void setAptp(String aptp) {
		this.aptp = aptp;
	}

	public String getSfmz() {
		return sfmz;
	}

	public void setSfmz(String sfmz) {
		this.sfmz = sfmz;
	}

	public String getHzje() {
		return hzje;
	}

	public void setHzje(String hzje) {
		this.hzje = hzje;
	}

	public String getDeleteState() {
		return deleteState;
	}

	public void setDeleteState(String deleteState) {
		this.deleteState = deleteState;
	}

	public String getCheckType() {
		return checkType;
	}

	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}

	public String getNowSubmitBatch() {
		return nowSubmitBatch;
	}

	public void setNowSubmitBatch(String nowSubmitBatch) {
		this.nowSubmitBatch = nowSubmitBatch;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getBwzt() {
		return bwzt;
	}

	public void setBwzt(String bwzt) {
		this.bwzt = bwzt;
	}

	public Date getRpdt() {
		return rpdt;
	}

	public void setRpdt(Date rpdt) {
		this.rpdt = rpdt;
	}

	public String getSubmitBatch() {
		return submitBatch;
	}

	public void setSubmitBatch(String submitBatch) {
		this.submitBatch = submitBatch;
	}

	public Date getOrpdt() {
		return orpdt;
	}

	public void setOrpdt(Date orpdt) {
		this.orpdt = orpdt;
	}

	public String getTname() {
		return tname;
	}

	public void setTname(String tname) {
		this.tname = tname;
	}

	public String getSubmitType() {
		return submitType;
	}

	public void setSubmitType(String submitType) {
		this.submitType = submitType;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BigAmount other = (BigAmount) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public LogConfig getLogConfig() {
		return new LogConfig("id");
	}

	@Override
	public String toString() {
		return "BigAmount [id=" + id + ", ricd=" + ricd + ", finc=" + finc + ", rlfc=" + rlfc + ", htdt=" + htdt
				+ ", crcd=" + crcd + ", csnm=" + csnm + ", citp=" + citp + ", oitp1=" + oitp1 + ", ctid=" + ctid
				+ ", ctnt=" + ctnt + ", ctvc=" + ctvc + ", cctl=" + cctl + ", ccei=" + ccei + ", ctar=" + ctar
				+ ", ctnm=" + ctnm + ", catp=" + catp + ", ctac=" + ctac + ", oatm=" + oatm + ", cbct=" + cbct
				+ ", ocbt=" + ocbt + ", cbcn=" + cbcn + ", tbnm=" + tbnm + ", tbit=" + tbit + ", oitp2=" + oitp2
				+ ", tbid=" + tbid + ", tbnt=" + tbnt + ", tstm=" + tstm + ", trcd=" + trcd + ", ticd=" + ticd
				+ ", rpmt=" + rpmt + ", rpmn=" + rpmn + ", tstp=" + tstp + ", octt=" + octt + ", ooct=" + ooct
				+ ", ocec=" + ocec + ", bptc=" + bptc + ", tsct=" + tsct + ", tsdr=" + tsdr + ", crpp=" + crpp
				+ ", crtp=" + crtp + ", crat=" + crat + ", crmb=" + crmb + ", cusd=" + cusd + ", cfin=" + cfin
				+ ", cfct=" + cfct + ", cfic=" + cfic + ", cfrc=" + cfrc + ", tcnm=" + tcnm + ", tcit=" + tcit
				+ ", oitp3=" + oitp3 + ", tcid=" + tcid + ", tcat=" + tcat + ", tcac=" + tcac + ", rotf1=" + rotf1
				+ ", rotf2=" + rotf2 + ", mirs=" + mirs + ", dwxx=" + dwxx + ", jgbm=" + jgbm + ", upState=" + upState
				+ ", queryDate=" + queryDate + ", starts=" + starts + ", ends=" + ends + ", aptp=" + aptp + ", sfmz="
				+ sfmz + ", hzje=" + hzje + ", deleteState=" + deleteState + ", checkType=" + checkType
				+ ", nowSubmitBatch=" + nowSubmitBatch + ", reportType=" + reportType + ", bwzt=" + bwzt + ", rpdt="
				+ rpdt + ", submitBatch=" + submitBatch + ", orpdt=" + orpdt + ", tname=" + tname + ", submitType="
				+ submitType + ", remarks=" + remarks + "]";
	}

}

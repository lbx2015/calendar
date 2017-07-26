package net.riking.entity.model;

import net.riking.core.entity.model.BaseFlowJob;

import javax.persistence.*;

import net.riking.core.entity.model.Job;

import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import net.riking.core.annos.Comment;
import net.riking.core.entity.ILog;
import net.riking.core.entity.LogConfig;

/**
 * Created by bing.xun on 2017/6/5.
 */
@Comment("反洗钱可疑")
@Entity
@Table(name = "T_AML_Suspicious")
public class AmlSuspicious extends BaseFlowJob implements ILog{
    /**
     *
     */
    private static final long serialVersionUID = -8081315218097840090L;

    public AmlSuspicious(){

    }

    public AmlSuspicious(Long id, String curJobState){
        this.id = id;
        this.job = new Job();
        this.job.setCurJobState(curJobState);
    }

    @Comment("id")
    @Id
    @Column(name = "id")
    @GeneratedValue
    private Long id;

    /** 报告机构编码 */
    @Comment("报告机构编码 ")
    @Column(name = "ricd", length = 14)
    private String ricd;

    /** 上报网点代码 */
    @Comment("上报网点代码")
    @Column(name = "rpnc", length = 16)
    private String rpnc;

    /** 金融机构网点代码 */
    @Comment("金融机构网点代码")
    @Column(name = "finc", length = 16)
    private String finc;

    /** 金融机构与客户的关系 */
    @Comment("金融机构与客户的关系")
    @Column(name = "rlfc", length = 2)
    private String rlfc;

    /** 可疑交易报告紧急程度 */
    @Comment("可疑交易报告紧急程度")
    @Column(name = "detr", length = 2)
    private String detr;

    /** 报送次数标志 */
    @Comment("报送次数标志 ")
    @Column(name = "torp", length = 20)
    private String torp;

    /** 初次报送的可疑交易报告报文名称 */
    @Comment("初次报送的可疑交易报告报文名称")
    @Column(name = "orxn", length = 64)
    private String orxn;

    /** 可疑交易报告的填报人员 */
    @Comment("可疑交易报告的填报人员")
    @Column(name = "rpnm", length = 16)
    private String rpnm;

    /** 客户号 (可疑主体客户号) */
    @Comment("客户号 (可疑主体客户号)")
    @Column(name = "csnm1", length = 32)
    private String csnm1;

    /** 可疑主体职业（对私）或行业（对公）*/
    @Comment("可疑主体职业（对私）或行业（对公）")
    @Column(name = "sevc", length = 32)
    private String sevc;

    /** 可疑主体姓名名称*/
    @Comment("可疑主体姓名名称")
    @Column(name = "senm", length = 512)
    private String senm;

    /** 可疑主体身份证件证明文件类型*/
    @Comment("可疑主体身份证件证明文件类型")
    @Column(name = "setp", length = 6)
    private String setp;

    /** 其他身份证件证明文件类型（可疑主体其他身份证件证明文件类型说明）*/
    @Comment("其他身份证件证明文件类型（可疑主体其他身份证件证明文件类型说明）")
    @Column(name = "oitp1", length = 32)
    private String oitp1;

    /** 可疑主体身份证件证明文件号码*/
    @Comment("可疑主体身份证件证明文件号码")
    @Column(name = "seid", length = 128)
    private String seid;

    /** 可疑主体国籍 1*/
    @Comment("可疑主体国籍 1")
    @Column(name = "stnt", length = 30)
    private String stnt;

    /** 可疑主体联系电话 1*/
    @Comment("可疑主体联系电话 1")
    @Column(name = "sctl", length = 64)
    private String sctl;

    /** 可疑主体住址经营地址 1*/
    @Comment("可疑主体住址经营地址 1")
    @Column(name = "sear", length = 512)
    private String sear;

    /** 可疑主体其他联系方式 1*/
    @Comment("可疑主体其他联系方式 1")
    @Column(name = "seei", length = 512)
    private String seei;

    /** 可疑主体法定代表人姓名 1*/
    @Comment("可疑主体法定代表人姓名 1")
    @Column(name = "srnm", length = 512)
    private String srnm;

    /** 可疑主体法定代表人身份证件类型*/
    @Comment("可疑主体法定代表人身份证件类型")
    @Column(name = "srit", length = 32)
    private String srit;

    /** 可疑主体法定代表人其他身份证件证明文件类型*/
    @Comment("可疑主体法定代表人其他身份证件证明文件类型")
    @Column(name = "orit", length = 32)
    private String orit;

    /** 可疑主体法定代表人身份证件号码*/
    @Comment("可疑主体法定代表人身份证件号码")
    @Column(name = "srid", length = 128)
    private String srid;

    /** 可疑主体控股股东或实际控制人名称*/
    @Comment("可疑主体控股股东或实际控制人名称")
    @Column(name = "scnm", length = 512)
    private String scnm;

    /** 可疑主体控股股东或实际控制人身份证件证明文件类型*/
    @Comment(" 可疑主体控股股东或实际控制人身份证件证明文件类型")
    @Column(name = "scit", length = 6)
    private String scit;

    /** 可疑主体控股股东或实际控制人其他身份证件证明文件类型*/
    @Comment("可疑主体控股股东或实际控制人其他身份证件证明文件类型")
    @Column(name = "ocit", length = 32)
    private String ocit;

    /** 可疑主体控股股东或实际控制人身份证件证明文件号码*/
    @Comment("可疑主体控股股东或实际控制人身份证件证明文件号码")
    @Column(name = "scid", length = 128)
    private String scid;

    /** 客户姓名名称*/
    @Comment("客户姓名名称")
    @Column(name = "ctnm", length = 512)
    private String ctnm;

    /** 客户身份证件证明文件类型*/
    @Comment("客户身份证件证明文件类型")
    @Column(name = "citp", length = 6)
    private String citp;

    /** （客户其他身份证件证明文件类型说明）其他身份证件证明文件类型*/
    @Comment("（客户其他身份证件证明文件类型说明）其他身份证件证明文件类型")
    @Column(name = "oitp2", length = 32)
    private String oitp2;

    /** 客户身份证件证明文件号码*/
    @Comment("客户身份证件证明文件号码")
    @Column(name = "ctid", length = 128)
    private String ctid;

    /** 客户号*/
    @Comment("客户号")
    @Column(name = "csnm2", length = 32)
    private String csnm2;

    /** 客户账户类型*/
    @Comment("客户账户类型")
    @Column(name = "catp", length = 6)
    private String catp;

    /** 客户账号*/
    @Comment("客户账号")
    @Column(name = "ctac", length = 64)
    private String ctac;

    /** 客户账户开立时间*/
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Comment("客户账户开立时间")
    @Temporal(TemporalType.DATE)
    @Column(name = "oatm")
    private Date oatm;

    /** 客户账户销户时间*/
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Comment("客户账户销户时间")
    @Temporal(TemporalType.DATE)
    @Column(name = "catm")
    private Date catm;

    /** 客户银行卡类型*/
    @Comment("客户银行卡类型")
    @Column(name = "cbct", length = 2)
    private String cbct;

    /** 客户银行卡其他类型*/
    @Comment("客户银行卡其他类型")
    @Column(name = "ocbt", length = 32)
    private String ocbt;

    /** 客户银行卡号码*/
    @Comment("客户银行卡号码")
    @Column(name = "cbcn", length = 64)
    private String cbcn;

    /** 交易代办人姓名*/
    @Comment("交易代办人姓名")
    @Column(name = "tbnm", length = 128)
    private String tbnm;

    /** 交易代办人身份证件证明文件类型*/
    @Comment("交易代办人身份证件证明文件类型")
    @Column(name = "tbit", length = 6)
    private String tbit;

    /** 其他身份证件证明文件类型（代办人其他身份证件证明文件类型）*/
    @Comment("其他身份证件证明文件类型（代办人其他身份证件证明文件类型）")
    @Column(name = "oitp3", length = 32)
    private String oitp3;

    /** 交易代办人身份证件证明文件号码*/
    @Comment("交易代办人身份证件证明文件号码")
    @Column(name = "tbid", length = 128)
    private String tbid;

    /** 交易代办人国籍*/
    @Comment("交易代办人国籍")
    @Column(name = "tbnt", length = 3)
    private String tbnt;

    /** 交易时间*/
    @Temporal(TemporalType.TIMESTAMP) 
    @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @Comment("交易时间")
    @Column(name = "tstm")
    private Date tstm;

    /** 交易发生地*/
    @Comment("交易发生地")
    @Column(name = "trcd", length = 9)
    private String trcd;

    /** 业务标识号*/
    @Comment(" 业务标识号")
    @Column(name = "ticd", length = 256)
    private String ticd;

    /** 收付款方匹配号类型*/
    @Comment("收付款方匹配号类型")
    @Column(name = "rpmt", length = 2)
    private String rpmt;

    /** 收付款方匹配号*/
    @Comment("收付款方匹配号")
    @Column(name = "rpmn", length = 500)
    private String rpmn;

    /** 交易方式*/
    @Comment("交易方式")
    @Column(name = "tstp", length = 6)
    private String tstp;

    /** 非柜台交易方式*/
    @Comment("非柜台交易方式")
    @Column(name = "octt", length = 2)
    private String octt;

    /** 其他非柜台交易方式*/
    @Comment("其他非柜台交易方式")
    @Column(name = "ooct", length = 32)
    private String ooct;

    /** 非柜台交易方式的设备代码*/
    @Comment("非柜台交易方式的设备代码")
    @Column(name = "ocec", length = 500)
    private String ocec;

    /** 银行与支付机构之间的业务交易编码*/
    @Comment("银行与支付机构之间的业务交易编码")
    @Column(name = "bptc", length = 500)
    private String bptc;

    /** 涉外收支交易分类与代码*/
    @Comment("涉外收支交易分类与代码")
    @Column(name = "tsct", length = 16)
    private String tsct;

    /** 资金收付标志*/
    @Comment("资金收付标志")
    @Column(name = "tsdr", length = 2)
    private String tsdr;

    /** 资金来源和用途*/
    @Comment("资金来源和用途")
    @Column(name = "crsp", length = 128)
    private String crsp;

    /** 交易币种*/
    @Comment(" 交易币种")
    @Column(name = "crtp", length = 3)
    private String crtp;

    /** 交易金额*/
    @Comment("交易金额")
    @Column(name = "crat", length = 20)
    private BigDecimal crat;

    /** 对方金融机构网点名称*/
    @Comment("对方金融机构网点名称")
    @Column(name = "cfin", length = 64)
    private String cfin;

    /** 对方金融机构网点代码类型*/
    @Comment("对方金融机构网点代码类型")
    @Column(name = "cfct", length = 2)
    private String cfct;

    /** 对方金融机构网点代码*/
    @Comment("对方金融机构网点代码")
    @Column(name = "cfic", length = 16)
    private String cfic;

    /** 对方金融机构网点行政区划代码*/
    @Comment("对方金融机构网点行政区划代码")
    @Column(name = "cfrc", length = 9)
    private String cfrc;

    /** 交易对手姓名名称*/
    @Comment("交易对手姓名名称")
    @Column(name = "tcnm", length = 128)
    private String tcnm;

    /** 交易对手身份证件证明文件类型*/
    @Comment("交易对手身份证件证明文件类型")
    @Column(name = "tcit", length = 6)
    private String tcit;

    /** 其他身份证件证明文件类型（交易对手其他身份证件证明文件类型说明）*/
    @Comment("其他身份证件证明文件类型（交易对手其他身份证件证明文件类型说明）")
    @Column(name = "oitp4", length = 32)
    private String oitp4;

    /** 交易对手身份证件证明文件号码*/
    @Comment("交易对手身份证件证明文件号码")
    @Column(name = "tcid", length = 128)
    private String tcid;

    /** 交易对手账户类型*/
    @Comment("交易对手账户类型")
    @Column(name = "tcat", length = 6)
    private String tcat;

    /** 交易对手账号*/
    @Comment("交易对手账号")
    @Column(name = "tcac", length = 64)
    private String tcac;

    /** 交易信息备注 1*/
    @Comment("交易信息备注 1")
    @Column(name = "rotf1", length = 64)
    private String rotf1;

    /** 交易信息备注 2*/
    @Comment(" 交易信息备注 2")
    @Column(name = "rotf2", length = 64)
    private String rotf2;

    /** 报送方向*/
    @Comment("报送方向")
    @Column(name = "dorp", length = 2)
    private String dorp;

    /** 其他报送方向*/
    @Comment("其他报送方向")
    @Column(name = "odrp", length = 32)
    private String odrp;

    /** 可疑交易报告触发点*/
    @Comment("可疑交易报告触发点")
    @Column(name = "tptr", length = 50)
    private String tptr;

    /** 其他可疑交易报告触发点(其他可疑交易报告触发点说明)*/
    @Comment("其他可疑交易报告触发点(其他可疑交易报告触发点说明)")
    @Column(name = "otpr", length = 2000)
    private String otpr;

    /** 资金交易及客户行为情况*/
    @Comment("资金交易及客户行为情况")
    @Lob
    @Column(name = "stcb", length = 10000)
    private String stcb;

    /** 疑点分析*/
    @Comment("疑点分析")
    @Lob
    @Column(name = "aosp", length = 10000)
    private String aosp;

    /** 疑似涉罪类型 1*/
    @Comment("疑似涉罪类型 1")
    @Column(name = "tosc", length = 200)
    private String tosc;

    /** 可疑交易特征代码 1*/
    @Comment("可疑交易特征代码 1")
    @Column(name = "stcr", length = 320)
    private String stcr;

    /** 人工补正标识*/
    @Comment("人工补正标识")
    @Column(name = "mirs", length = 64)
    private String mirs;

    /** 定位信息*/
    @Comment("定位信息")
    @Column(name = "dwxx", length = 128)
    private String dwxx;

    /** 可疑主体总数*/
    @Comment("可疑主体总数")
    @Column(name = "setn", length = 8)
    private Integer setn;

    /** 可疑交易总数*/
    @Comment("可疑交易总数")
    @Column(name = "stnm", length = 8)
    private Integer stnm;

    /** 机构编码 */
    @Comment("机构编码")
    @Column(name = "jgbm", length = 32)
    private String jgbm;
	
	/** 删除状态 */
    @Comment("删除状态")
	@Column(name = "deleteState",length=12)
	private String deleteState;
	
	/** 是否审核不通过标记 */
    @Comment("是否审核不通过标记")
	@Column(name = "aptp",length=12)
	private String aptp;
	
	/** 报送类型 */
    @Comment("报送类型")
	@Column(name = "reportType",length=12)
	private String reportType;
	
	/** 报送批次 */
    @Comment("报送批次")
	@Column(name = "submitBatch",length=12)
	private String submitBatch;
	
	/** 现报送批次 */
    @Comment("现报送批次 ")
	@Column(name = "nowSubmitBatch",length=12)
	private String nowSubmitBatch;
	
	/** 原报送时间 */
	@DateTimeFormat(pattern="yyyy-MM-dd")
    @Comment("原报送时间")
	@Temporal(TemporalType.DATE)
	@Column(name = "orpdt")
	private Date orpdt;
	
	/** 报文名称 */
    @Comment("报文名称")
	@Column(name = "tname",length=128)
	private String tname;
	
	/** 检验状态 */
    @Comment("检验状态")
	@Column(name = "checkType",length=12)
	private String checkType;
	
	/** 上报状态 */
    @Comment("上报状态 ")
	@Column(name = "submitType",length=12)
	private String submitType;
	
	/** 不上报备注 */
    @Comment("不上报备注 ")
	@Column(name = "remarks",length=400)
	private String remarks;
	
	/**查询开始时间*/
	@DateTimeFormat(pattern="yyyy-MM-dd")
    @Comment("查询开始时间")
	@Transient
	private Date starts;
	
	/**查询结束时间*/
	@DateTimeFormat(pattern="yyyy-MM-dd")
    @Comment("查询结束时间")
	@Transient
	private Date ends;
	
	/**查询时间类型*/
    @Comment("查询时间类型")
	@Transient
	private String queryDate;
	
	/**查询上报状态*/
    @Comment("查询上报状态")
	@Transient
	private String upState;

	/** 采集时间 */
	@DateTimeFormat(pattern="yyyy-MM-dd")
    @Comment("采集时间")
	@Temporal(TemporalType.DATE)
	@Column(name = "rpdt",length = 8)
	private Date rpdt;
	
	/** 是否入库  01未入库  02已入库 */
    @Comment("是否入库  01未入库  02已入库")
	@Column(name = "sfrk",length=12)
	private String sfrk;
	
	/** 报文状态 */
    @Comment("报文状态")
	@Column(name = "bwzt",length=12)
	private String bwzt;
	
	/** 可疑行为描述 */
    @Comment("可疑行为描述")
	@Column(name = "ssds", length = 500)
	private String ssds;
	
	/** 采取措施 */
    @Comment(" 采取措施 ")
	@Column(name = "tkms", length = 500)
	private String tkms;
	
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

    public String getRpnc() {
        return rpnc;
    }

    public void setRpnc(String rpnc) {
        this.rpnc = rpnc;
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

    public String getDetr() {
        return detr;
    }

    public void setDetr(String detr) {
        this.detr = detr;
    }

    public String getTorp() {
        return torp;
    }

    public void setTorp(String torp) {
        this.torp = torp;
    }

    public String getOrxn() {
        return orxn;
    }

    public void setOrxn(String orxn) {
        this.orxn = orxn;
    }

    public String getRpnm() {
        return rpnm;
    }

    public void setRpnm(String rpnm) {
        this.rpnm = rpnm;
    }

    public String getCsnm1() {
        return csnm1;
    }

    public void setCsnm1(String csnm1) {
        this.csnm1 = csnm1;
    }

    public String getSevc() {
        return sevc;
    }

    public void setSevc(String sevc) {
        this.sevc = sevc;
    }

    public String getSenm() {
        return senm;
    }

    public void setSenm(String senm) {
        this.senm = senm;
    }

    public String getSetp() {
        return setp;
    }

    public void setSetp(String setp) {
        this.setp = setp;
    }

    public String getOitp1() {
        return oitp1;
    }

    public void setOitp1(String oitp1) {
        this.oitp1 = oitp1;
    }

    public String getSeid() {
        return seid;
    }

    public void setSeid(String seid) {
        this.seid = seid;
    }

    public String getStnt() {
        return stnt;
    }

    public void setStnt(String stnt) {
        this.stnt = stnt;
    }

    public String getSctl() {
        return sctl;
    }

    public void setSctl(String sctl) {
        this.sctl = sctl;
    }

    public String getSear() {
        return sear;
    }

    public void setSear(String sear) {
        this.sear = sear;
    }

    public String getSeei() {
        return seei;
    }

    public void setSeei(String seei) {
        this.seei = seei;
    }

    public String getSrnm() {
        return srnm;
    }

    public void setSrnm(String srnm) {
        this.srnm = srnm;
    }

    public String getSrit() {
        return srit;
    }

    public void setSrit(String srit) {
        this.srit = srit;
    }

    public String getOrit() {
        return orit;
    }

    public void setOrit(String orit) {
        this.orit = orit;
    }

    public String getSrid() {
        return srid;
    }

    public void setSrid(String srid) {
        this.srid = srid;
    }

    public String getScnm() {
        return scnm;
    }

    public void setScnm(String scnm) {
        this.scnm = scnm;
    }

    public String getScit() {
        return scit;
    }

    public void setScit(String scit) {
        this.scit = scit;
    }

    public String getOcit() {
        return ocit;
    }

    public void setOcit(String ocit) {
        this.ocit = ocit;
    }

    public String getScid() {
        return scid;
    }

    public void setScid(String scid) {
        this.scid = scid;
    }

    public String getCtnm() {
        return ctnm;
    }

    public void setCtnm(String ctnm) {
        this.ctnm = ctnm;
    }

    public String getCitp() {
        return citp;
    }

    public void setCitp(String citp) {
        this.citp = citp;
    }

    public String getOitp2() {
        return oitp2;
    }

    public void setOitp2(String oitp2) {
        this.oitp2 = oitp2;
    }

    public String getCtid() {
        return ctid;
    }

    public void setCtid(String ctid) {
        this.ctid = ctid;
    }

    public String getCsnm2() {
        return csnm2;
    }

    public void setCsnm2(String csnm2) {
        this.csnm2 = csnm2;
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

    public Date getCatm() {
        return catm;
    }

    public void setCatm(Date catm) {
        this.catm = catm;
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

    public String getOitp3() {
        return oitp3;
    }

    public void setOitp3(String oitp3) {
        this.oitp3 = oitp3;
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

    public String getCrsp() {
        return crsp;
    }

    public void setCrsp(String crsp) {
        this.crsp = crsp;
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

    public String getOitp4() {
        return oitp4;
    }

    public void setOitp4(String oitp4) {
        this.oitp4 = oitp4;
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

    public String getDorp() {
        return dorp;
    }

    public void setDorp(String dorp) {
        this.dorp = dorp;
    }

    public String getOdrp() {
        return odrp;
    }

    public void setOdrp(String odrp) {
        this.odrp = odrp;
    }

    public String getTptr() {
        return tptr;
    }

    public void setTptr(String tptr) {
        this.tptr = tptr;
    }

    public String getOtpr() {
        return otpr;
    }

    public void setOtpr(String otpr) {
        this.otpr = otpr;
    }

    public String getStcb() {
        return stcb;
    }

    public void setStcb(String stcb) {
        this.stcb = stcb;
    }

    public String getAosp() {
        return aosp;
    }

    public void setAosp(String aosp) {
        this.aosp = aosp;
    }

    public String getTosc() {
        return tosc;
    }

    public void setTosc(String tosc) {
        this.tosc = tosc;
    }

    public String getStcr() {
        return stcr;
    }

    public void setStcr(String stcr) {
        this.stcr = stcr;
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

    public Integer getSetn() {
        return setn;
    }

    public void setSetn(Integer setn) {
        this.setn = setn;
    }

    public Integer getStnm() {
        return stnm;
    }

    public void setStnm(Integer stnm) {
        this.stnm = stnm;
    }

    public String getJgbm() {
        return jgbm;
    }

    public void setJgbm(String jgbm) {
        this.jgbm = jgbm;
    }

	public String getDeleteState() {
		return deleteState;
	}

	public void setDeleteState(String deleteState) {
		this.deleteState = deleteState;
	}

	public String getAptp() {
		return aptp;
	}

	public void setAptp(String aptp) {
		this.aptp = aptp;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getSubmitBatch() {
		return submitBatch;
	}

	public void setSubmitBatch(String submitBatch) {
		this.submitBatch = submitBatch;
	}

	public String getNowSubmitBatch() {
		return nowSubmitBatch;
	}

	public void setNowSubmitBatch(String nowSubmitBatch) {
		this.nowSubmitBatch = nowSubmitBatch;
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

	public String getCheckType() {
		return checkType;
	}

	public void setCheckType(String checkType) {
		this.checkType = checkType;
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

	public String getQueryDate() {
		return queryDate;
	}

	public void setQueryDate(String queryDate) {
		this.queryDate = queryDate;
	}

	public String getUpState() {
		return upState;
	}

	public void setUpState(String upState) {
		this.upState = upState;
	}

	public Date getRpdt() {
		return rpdt;
	}

	public void setRpdt(Date rpdt) {
		this.rpdt = rpdt;
	}

	public String getSfrk() {
		return sfrk;
	}

	public void setSfrk(String sfrk) {
		this.sfrk = sfrk;
	}

	public String getBwzt() {
		return bwzt;
	}

	public void setBwzt(String bwzt) {
		this.bwzt = bwzt;
	}

	public String getSsds() {
		return ssds;
	}

	public void setSsds(String ssds) {
		this.ssds = ssds;
	}

	public String getTkms() {
		return tkms;
	}

	public void setTkms(String tkms) {
		this.tkms = tkms;
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
        AmlSuspicious other = (AmlSuspicious) obj;
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

}

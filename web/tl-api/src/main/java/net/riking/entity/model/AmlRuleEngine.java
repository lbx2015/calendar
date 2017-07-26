package net.riking.entity.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import net.riking.core.annos.Comment;
import net.riking.core.entity.ILog;
import net.riking.core.entity.LogConfig;
@Comment("规则引擎")
@Entity
@Table(name = "T_AML_RuleEngine")
public class AmlRuleEngine implements ILog{

    public static Map<Integer, String> enabledMap = new LinkedHashMap<Integer, String>();

    public static Map<Integer, String> typeMap = new LinkedHashMap<Integer, String>();

    static {
        enabledMap.put(1, "启用");
        enabledMap.put(0, "禁用");

        typeMap.put(1, "大额");
        typeMap.put(2, "可疑");
    }

    /** 系统ID */
    @Comment("系统ID")
    @Id
    @Column(name = "id")
    @GeneratedValue
    private Long id;

    /** 规则名称 */
    @Comment("规则名称")
    @Column(name = "Rule_Name", length = 255)
    private String ruleName;

    /** 规则编号 */
    @Comment("规则编号")
    @Column(name = "Rule_No", length = 100)
    private String ruleNo;

    /** 使用状态1-启用，0-禁用 */
    @Comment("使用状态1-启用，0-禁用")
    @Column(name = "Enabled", length = 1)
    private Integer enabled;

    /** 审核状态1-审核通过，0-审核不通过 */
    @Column(name = "approved", length = 1)
    private Integer approved;

    @Transient
    private String enabledDes;

    /** 采集类型1-大额，2-可疑 */
    @Comment("采集类型1-大额，2-可疑")
    @Column(name = "Type", length = 1)
    private Integer type;

    @Transient
    private String typeDes;

    /** 创建时间 */
    @Comment("创建时间")
    @Temporal(TemporalType.DATE)
    @Column(name = "Date_Create")
    private Date dateCreate;

    /** 创建人 */
    @Comment("创建人")
    @Column(name = "person_create", length = 100)
    private String personCreate;

    /** 修改时间 */
    @Comment("修改时间")
    @Temporal(TemporalType.DATE)
    @Column(name = "Date_Modify")
    private Date dateModify;

    /** 修改人 */
    @Comment("修改人")
    @Column(name = "person_modify", length = 100)
    private String personModify;

    /** 表达式中文能显示 */
    @Comment("表达式中文能显示")
    @Column(name = "Rule_Express_Name", length = 1000)
    private String ruleExpressName;

    /** 表达式实际代码 */
    @Comment("表达式实际代码")
    @Column(name = "Rule_Express_Coder", length = 1000)
    private String ruleExpressCoder;

    //交易区间
    @Comment("交易区间")
    @Column(name = "date_range")
    private Integer dateRange;

    //支出笔数
    @Comment("支出笔数")
    @Column(name = "debit_count")
    private Integer debitCount;

    //支出笔数条件
    @Comment("支出笔数条件")
    @Column(name = "debit_count_condition")
    private String debitCountCondition;

    //收入笔数
    @Comment("收入笔数")
    @Column(name = "credit_count")
    private Integer creditCount;

    //收入笔数条件
    @Comment("/收入笔数条件")
    @Column(name = "credit_count_condition")
    private String creditCountCondition;

    //支出总金额条件
    @Comment("支出总金额条件")
    @Column(name = "debit_amount_condition")
    private String debitAmountCondition;

    //支出总金额
    @Comment("支出总金额")
    @Column(name = "debit_amount")
    private BigDecimal debitAmount;

    //收入总金额条件
    @Comment("收入总金额条件")
    @Column(name = "credit_amount_condition")
    private String creditAmountCondition;

    //收入总金额
    @Comment("收入总金额")
    @Column(name = "credit_amount")
    private BigDecimal creditAmount;

    //连续天数
    @Comment("连续天数")
    @Column(name = "continuous_days")
    private Integer continuousDays;

    //收支比
    @Comment("收支比")
    @Column(name = "debit_credit_percent")
    private Float debitCreditPercent;

    //支收比
    @Comment("支收比")
    @Column(name = "credit_debit_percent")
    private Float creditDebitPercent;

    //支收比条件
    @Comment("支收比条件")
    @Column(name = "debit_CPC",length=2)
    private String debitCreditPercentCondition;

    //收支比条件
    @Comment("收支比条件")
    @Column(name = "credit_DPC",length=2)
    private String creditDebitPercentCondition;
    
    
    @Column(name = "debit_credit_count_andor")
    private String debitCreditCountAndor;
    
    
    @Column(name = "debit_credit_amount_andor")
    private String debitCreditAmountAndor;

   
    @Column(name = "debit_credit_percent_andor")
    private String debitCreditPercentAndor;

    
    @Column(name = "dollar_DCAA")
    private String dollarDebitCreditAmountAndor;

    //收入美金限额
    @Comment("收入美金限额")
    @Column(name = "dollar_credit_amount")
    private BigDecimal dollarCreditAmount;

    //支出美金限额
    @Comment("支出美金限额")
    @Column(name = "dollar_debit_amount")
    private BigDecimal dollarDebitAmount;


    //收入美金限额
    @Comment("收入美金限额")
    @Column(name = "dollar_credit_amount_condition",length=2)
    private String dollarCreditAmountCondition;

    //支出美金限额
    @Comment("支出美金限额")
    @Column(name = "dollar_debit_amount_condition",length=2)
    private String dollarDebitAmountCondition;

    //特征码
    @Comment("特征码")
    @Column(name = "stcr_crcd")
    private String stcrCrcd;

    //是否有相同交易对手
    @Comment("是否有相同交易对手")
    @Column(name = "same_counterparties")
    private String sameCounterparties;

    //总交易笔数
    @Comment("总交易笔数")
    @Column(name = "total_count")
    private Integer totalCount;

    //间隔天数
    @Comment("间隔天数")
    @Column(name = "interval_days")
    private Integer intervalDays;

    //总交易笔数条件
    @Comment("总交易笔数条件")
    @Column(name="total_count_condition",length=2)
    private String totalCountCondition;

    //间隔天数条件
    @Comment("间隔天数条件")
    @Column(name="interval_days_condition", length=2)
    private String intervalDaysCondition;

    /* @Column(name="crime_type_id")
    private Long crimeTypeId;

    //黑名单匹配属性
    @Transient
    private String blackProps;

    *//** 名单类型 *//*
    @Transient
    private String hmdlx;

    *//** 名单来源 *//*
    @Transient
    private String mdly;

    //分值
    @Transient
    private Integer fz;
**/
    //客户类型 1.对公 ，2 对私
    @Comment("客户类型 1.对公 ，2 对私")
    @Column(name="khlx")
    private Byte khlx;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRuleNo() {
        return ruleNo;
    }

    public void setRuleNo(String ruleNo) {
        this.ruleNo = ruleNo;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public String getEnabledDes() {
        if (this.enabled != null)
            return enabledMap.get(this.enabled);
        return enabledDes;
    }

    public void setEnabledDes(String enabledDes) {
        this.enabledDes = enabledDes;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTypeDes() {
        if (this.type != null)
            return typeMap.get(this.type);
        return typeDes;
    }

    public void setTypeDes(String typeDes) {
        this.typeDes = typeDes;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    public Date getDateModify() {
        return dateModify;
    }

    public void setDateModify(Date dateModify) {
        this.dateModify = dateModify;
    }

    public String getPersonCreate() {
        return personCreate;
    }

    public void setPersonCreate(String personCreate) {
        this.personCreate = personCreate;
    }

    public String getPersonModify() {
        return personModify;
    }

    public void setPersonModify(String personModify) {
        this.personModify = personModify;
    }

    public String getRuleExpressName() {
        return ruleExpressName;
    }

    public void setRuleExpressName(String ruleExpressName) {
        this.ruleExpressName = ruleExpressName;
    }

    public String getRuleExpressCoder() {
        return ruleExpressCoder;
    }

    public void setRuleExpressCoder(String ruleExpressCoder) {
        this.ruleExpressCoder = ruleExpressCoder;
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
        AmlRuleEngine other = (AmlRuleEngine) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    public Integer getDateRange() {
        return dateRange;
    }

    public void setDateRange(Integer dateRange) {
        this.dateRange = dateRange;
    }

    public Integer getDebitCount() {
        return debitCount;
    }

    public void setDebitCount(Integer debitCount) {
        this.debitCount = debitCount;
    }

    public String getDebitCountCondition() {
        return debitCountCondition;
    }

    public void setDebitCountCondition(String debitCountCondition) {
        this.debitCountCondition = debitCountCondition;
    }

    public Integer getCreditCount() {
        return creditCount;
    }

    public void setCreditCount(Integer creditCount) {
        this.creditCount = creditCount;
    }

    public String getCreditCountCondition() {
        return creditCountCondition;
    }

    public void setCreditCountCondition(String creditCountCondition) {
        this.creditCountCondition = creditCountCondition;
    }

    public String getDebitAmountCondition() {
        return debitAmountCondition;
    }

    public void setDebitAmountCondition(String debitAmountCondition) {
        this.debitAmountCondition = debitAmountCondition;
    }

    public BigDecimal getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(BigDecimal debitAmount) {
        this.debitAmount = debitAmount;
    }

    public String getCreditAmountCondition() {
        return creditAmountCondition;
    }

    public void setCreditAmountCondition(String creditAmountCondition) {
        this.creditAmountCondition = creditAmountCondition;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }

    public Float getDebitCreditPercent() {
        return debitCreditPercent;
    }

    public void setDebitCreditPercent(Float debitCreditPercent) {
        this.debitCreditPercent = debitCreditPercent;
    }

    public Float getCreditDebitPercent() {
        return creditDebitPercent;
    }

    public void setCreditDebitPercent(Float creditDebitPercent) {
        this.creditDebitPercent = creditDebitPercent;
    }

    public String getDebitCreditPercentCondition() {
        return debitCreditPercentCondition;
    }

    public void setDebitCreditPercentCondition(String debitCreditPercentCondition) {
        this.debitCreditPercentCondition = debitCreditPercentCondition;
    }

    public String getCreditDebitPercentCondition() {
        return creditDebitPercentCondition;
    }

    public void setCreditDebitPercentCondition(String creditDebitPercentCondition) {
        this.creditDebitPercentCondition = creditDebitPercentCondition;
    }

    public Integer getContinuousDays() {
        return continuousDays;
    }

    public void setContinuousDays(Integer continuousDays) {
        this.continuousDays = continuousDays;
    }

    public String getDebitCreditCountAndor() {
        return debitCreditCountAndor;
    }

    public void setDebitCreditCountAndor(String debitCreditCountAndor) {
        this.debitCreditCountAndor = debitCreditCountAndor;
    }

    public String getDebitCreditAmountAndor() {
        return debitCreditAmountAndor;
    }

    public void setDebitCreditAmountAndor(String debitCreditAmountAndor) {
        this.debitCreditAmountAndor = debitCreditAmountAndor;
    }

    public String getDebitCreditPercentAndor() {
        return debitCreditPercentAndor;
    }

    public void setDebitCreditPercentAndor(String debitCreditPercentAndor) {
        this.debitCreditPercentAndor = debitCreditPercentAndor;
    }

    public String getStcrCrcd() {
        return stcrCrcd;
    }

    public void setStcrCrcd(String stcrCrcd) {
        this.stcrCrcd = stcrCrcd;
    }

    public String getDollarCreditAmountCondition() {
        return dollarCreditAmountCondition;
    }

    public void setDollarCreditAmountCondition(String dollarCreditAmountCondition) {
        this.dollarCreditAmountCondition = dollarCreditAmountCondition;
    }

    public String getDollarDebitAmountCondition() {
        return dollarDebitAmountCondition;
    }

    public void setDollarDebitAmountCondition(String dollarDebitAmountCondition) {
        this.dollarDebitAmountCondition = dollarDebitAmountCondition;
    }

    public String getDollarDebitCreditAmountAndor() {
        return dollarDebitCreditAmountAndor;
    }

    public void setDollarDebitCreditAmountAndor(String dollarDebitCreditAmountAndor) {
        this.dollarDebitCreditAmountAndor = dollarDebitCreditAmountAndor;
    }

    public BigDecimal getDollarCreditAmount() {
        return dollarCreditAmount;
    }

    public void setDollarCreditAmount(BigDecimal dollarCreditAmount) {
        this.dollarCreditAmount = dollarCreditAmount;
    }

    public BigDecimal getDollarDebitAmount() {
        return dollarDebitAmount;
    }

    public void setDollarDebitAmount(BigDecimal dollarDebitAmount) {
        this.dollarDebitAmount = dollarDebitAmount;
    }

    public String getSameCounterparties() {
        return sameCounterparties;
    }

    public void setSameCounterparties(String sameCounterparties) {
        this.sameCounterparties = sameCounterparties;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getIntervalDays() {
        return intervalDays;
    }

    public void setIntervalDays(Integer intervalDays) {
        this.intervalDays = intervalDays;
    }

    public String getTotalCountCondition() {
        return totalCountCondition;
    }

    public void setTotalCountCondition(String totalCountCondition) {
        this.totalCountCondition = totalCountCondition;
    }

    public String getIntervalDaysCondition() {
        return intervalDaysCondition;
    }

    public void setIntervalDaysCondition(String intervalDaysCondition) {
        this.intervalDaysCondition = intervalDaysCondition;
    }

    public Integer getApproved() {
        return approved;
    }

    public void setApproved(Integer approved) {
        this.approved = approved;
    }

    public Byte getKhlx() {
        return khlx;
    }

    public void setKhlx(Byte khlx) {
        this.khlx = khlx;
    }

    @Override
    public LogConfig getLogConfig() {
        return new LogConfig("id");
    }
}

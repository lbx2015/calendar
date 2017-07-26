package net.riking.entity.report;

import java.util.List;
import java.util.Map;

/**
 * Created by bing.xun on 2017/7/11.
 */
public class Report {
    //所属行
    private String BranchName;

    //报送日期
    private String reportDate;

    //向中国反洗钱监测分析中心报送大额交易份数（总份数）
    private Long bigAmountTotal;

    //向中国反洗钱监测分析中心报送大额交易份数（补正份数）
    private Long bigAmountBZTotal;

    //向中国反洗钱监测分析中心报送可疑交易份数（总份数）
    private Long suspiciousTotal;

    //向中国反洗钱监测分析中心报送可疑交易份数（报中心份数）
    private Long suspiciousBZXTotal;

    //向中国反洗钱监测分析中心报送可疑交易份数（报中心和人民银行当地分支机构份数）
    private Long suspiciousBZXHDDRMYHFZJGTotal;

    //向中国反洗钱监测分析中心报送可疑交易份数（报中心和当地公安份数）
    private Long suspiciousBZXHDDGATotal;

    //向中国反洗钱监测分析中心报送可疑交易份数（报中心、人民银行当地分支机构和当地公安）
    private Long suspiciousBZXHDDRMYHFZJGHDDGATotal;

    //大额笔数（按客户）
    private Map<String, List<CustomerReport>> bigAmountTotalMap;

    //可疑笔数（按客户）
    private Map<String, List<CustomerReport>> suspiciousTotalMap;

    //大额交易笔数（按大额交易特征代码0501)
    private Long bigAmount0501Total;

    //大额交易笔数（按大额交易特征代码0502)
    private Long bigAmount0502Total;

    //大额交易笔数（按大额交易特征代码0503)
    private Long bigAmount0503Total;

    //大额交易笔数（按大额交易特征代码0504)
    private Long bigAmount0504Total;

    //疑似涉罪类型
    private List<CrimeReport> crimeReportList;


    public String getBranchName() {
        return BranchName;
    }

    public void setBranchName(String branchName) {
        BranchName = branchName;
    }


    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public Long getBigAmountTotal() {
        return bigAmountTotal;
    }

    public void setBigAmountTotal(Long bigAmountTotal) {
        this.bigAmountTotal = bigAmountTotal;
    }

    public Long getBigAmountBZTotal() {
        return bigAmountBZTotal;
    }

    public void setBigAmountBZTotal(Long bigAmountBZTotal) {
        this.bigAmountBZTotal = bigAmountBZTotal;
    }

    public Long getSuspiciousTotal() {
        return suspiciousTotal;
    }

    public void setSuspiciousTotal(Long suspiciousTotal) {
        this.suspiciousTotal = suspiciousTotal;
    }

    public Long getSuspiciousBZXTotal() {
        return suspiciousBZXTotal;
    }

    public void setSuspiciousBZXTotal(Long suspiciousBZXTotal) {
        this.suspiciousBZXTotal = suspiciousBZXTotal;
    }

    public Long getSuspiciousBZXHDDRMYHFZJGTotal() {
        return suspiciousBZXHDDRMYHFZJGTotal;
    }

    public void setSuspiciousBZXHDDRMYHFZJGTotal(Long suspiciousBZXHDDRMYHFZJGTotal) {
        this.suspiciousBZXHDDRMYHFZJGTotal = suspiciousBZXHDDRMYHFZJGTotal;
    }

    public Long getSuspiciousBZXHDDGATotal() {
        return suspiciousBZXHDDGATotal;
    }

    public void setSuspiciousBZXHDDGATotal(Long suspiciousBZXHDDGATotal) {
        this.suspiciousBZXHDDGATotal = suspiciousBZXHDDGATotal;
    }

    public Long getSuspiciousBZXHDDRMYHFZJGHDDGATotal() {
        return suspiciousBZXHDDRMYHFZJGHDDGATotal;
    }

    public void setSuspiciousBZXHDDRMYHFZJGHDDGATotal(Long suspiciousBZXHDDRMYHFZJGHDDGATotal) {
        this.suspiciousBZXHDDRMYHFZJGHDDGATotal = suspiciousBZXHDDRMYHFZJGHDDGATotal;
    }

    public Map<String, List<CustomerReport>> getBigAmountTotalMap() {
        return bigAmountTotalMap;
    }

    public void setBigAmountTotalMap(Map<String, List<CustomerReport>> bigAmountTotalMap) {
        this.bigAmountTotalMap = bigAmountTotalMap;
    }

    public Map<String, List<CustomerReport>> getSuspiciousTotalMap() {
        return suspiciousTotalMap;
    }

    public void setSuspiciousTotalMap(Map<String, List<CustomerReport>> suspiciousTotalMap) {
        this.suspiciousTotalMap = suspiciousTotalMap;
    }

    public Long getBigAmount0501Total() {
        return bigAmount0501Total;
    }

    public void setBigAmount0501Total(Long bigAmount0501Total) {
        this.bigAmount0501Total = bigAmount0501Total;
    }

    public Long getBigAmount0502Total() {
        return bigAmount0502Total;
    }

    public void setBigAmount0502Total(Long bigAmount0502Total) {
        this.bigAmount0502Total = bigAmount0502Total;
    }

    public Long getBigAmount0503Total() {
        return bigAmount0503Total;
    }

    public void setBigAmount0503Total(Long bigAmount0503Total) {
        this.bigAmount0503Total = bigAmount0503Total;
    }

    public Long getBigAmount0504Total() {
        return bigAmount0504Total;
    }

    public void setBigAmount0504Total(Long bigAmount0504Total) {
        this.bigAmount0504Total = bigAmount0504Total;
    }

    public List<CrimeReport> getCrimeReportList() {
        return crimeReportList;
    }

    public void setCrimeReportList(List<CrimeReport> crimeReportList) {
        this.crimeReportList = crimeReportList;
    }
}

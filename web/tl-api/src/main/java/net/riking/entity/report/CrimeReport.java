package net.riking.entity.report;

/**
 * Created by bing.xun on 2017/7/11.
 */
public class CrimeReport {

    //涉罪类型
    private String crime;

    //上报份数
    private Long reportTotal;

    //已上报份数
    private Long reportedTotal;

    //总份数
    private Long total;

    public String getCrime() {
        return crime;
    }

    public void setCrime(String crime) {
        this.crime = crime;
    }

    public Long getReportTotal() {
        return reportTotal;
    }

    public void setReportTotal(Long reportTotal) {
        this.reportTotal = reportTotal;
    }

    public Long getReportedTotal() {
        return reportedTotal;
    }

    public void setReportedTotal(Long reportedTotal) {
        this.reportedTotal = reportedTotal;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}

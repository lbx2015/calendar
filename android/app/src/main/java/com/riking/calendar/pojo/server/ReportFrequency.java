package com.riking.calendar.pojo.server;

public class ReportFrequency {
    public String reportId;
    public String reportName;
    public String reportTitle;
    public String isComplete;
    public String strFrency;
    public String isSubscribe;

    @Override
    public int hashCode() {
        int result = reportId.hashCode();
        result = 31 * result + reportName.hashCode();
        result = 31 * result + reportTitle.hashCode();
        result = 31 * result + isComplete.hashCode();
        result = 31 * result + strFrency.hashCode();
        result = 31 * result + isSubscribe.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        boolean b = super.equals(obj);
        if (b) {
            return b;
        }
        if (obj == null) {
            return false;
        }
        if (reportId.equals(((ReportFrequency) obj).reportId)) {
            return true;
        }
        return false;
    }
}

package com.riking.calendar.pojo.server;

import java.util.List;

public class ReportListResult {
    //	@Comment("机构编号")
    public String agenceCode;

    //	@Comment("机构中文名称")
    public String agenceName;

    public List<ReportTypeListResult> list;

/*

    public String getAgenceName() {
        if (agenceCode.toUpperCase().equals("CBRC")) {
            agenceName = "银监会";
        } else if (agenceCode.toUpperCase().equals("PBOC")) {
            agenceName = "中国人民银行";
        }
        return agenceName;
    }
*/

}

package com.riking.calendar.pojo.server;

import java.util.List;

public class ReportTypeListResult {
    //	@Comment("机构编号")
    public String agenceCode;

    //	@Comment("报表所属模块")
    public String moduleType;

    //	@Comment("报表所属模块名称")
    public String moduleTypeName;

    public List<ReportResult> list;


}

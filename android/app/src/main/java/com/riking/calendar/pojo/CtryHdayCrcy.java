package com.riking.calendar.pojo;

import java.util.Date;

public class CtryHdayCrcy {
    //query string
    public String queryParam;
    //各国节假日币种表
    public String id;
    //	@Comment("国家/地区名称")
//	@Column(name = "ctry_name", length = 32)
    public String ctryName;
    public String ctryNameValue;
    //	@Comment("节假日名称")
//	@Column(name = "hday_name", length = 32)
    public String hdayName;
    public String hdayNameValue;

    //	@Comment("节假日时间")
//	@Temporal(TemporalType.DATE)
//	@DateTimeFormat(pattern = "yyyyMMdd")
//	@Column(name = "hday_date")
    public String hdayDate;

    //	@Comment("币种")
//	@Column(name = "crcy", length = 3)
    public String crcy;

    //	@Comment("备注信息")
//	@Column(name = "remark", length = 500)
    public String remark;

    //0-删除状态   1-未删除状态
//	@Comment("删除标记")
//	@Column(name = "delete_state")
    public String deleteState;

    //	@Transient
    public String iconUrl;
    public byte pindex;
    public byte pcount;

    @Override
    public String toString() {
        return "CtryHdayCrcy{" +
                "id='" + id + '\'' +
                ", ctryName='" + ctryName + '\'' +
                ", hdayName='" + hdayName + '\'' +
                ", hdayDate=" + hdayDate +
                ", crcy='" + crcy + '\'' +
                ", remark='" + remark + '\'' +
                ", deleteState='" + deleteState + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                '}';
    }
}

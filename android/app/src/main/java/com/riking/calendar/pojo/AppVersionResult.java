package com.riking.calendar.pojo;

public class AppVersionResult {

    //	@Comment("物理主键")
//	@Id
//	@Column(name = "id", length = 32)
//	@GenericGenerator(name = "system-uuid", strategy = "uuid")
//	@GeneratedValue(generator = "system-uuid")
//	@JsonProperty("appVersionId")
//    public String appVersionId;

    //	@Comment("app版本号")
//	@Column(name = "version_no", length = 32)
    public String versionNo;

    //	@Comment("移动端：1-IOS；2-Android")
//	@Column(name = "client_type", length = 1)
//    public int clientType = 2;

    //	@Comment("是否强制更新(0-不强制；1-强制)")
//	@Column(name = "enforce")
    public int enforce = -1;

    //	@Comment("用户状态 0-禁用 1-启用")
//	@org.hibernate.annotations.ColumnDefault("0")
//	@Column(name = "enabled", nullable = false, precision = 1)
//    public int enabled = -1;

    //	@Comment("下载更新地址")
//	@Column(name = "url", length = 255)
    public String url;

    //	@Comment("备注")
//	@Column(name = "remark", length = 255)
    public String remark;

    //	@Comment("操作类型：edit-修改;add-新增")
//	@Transient
//    public String opt;

}

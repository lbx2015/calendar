package com.riking.calendar.pojo;

//@Entity
//@Table(name = "t_app_user")
public class AppUser {
    //用户表
//    @Id
//    @Column(name = "Id", length = 32)
//    @GenericGenerator(name = "system-uuid", strategy = "uuid")
//    @GeneratedValue(generator = "system-uuid")
    public String userId;

    //    @Comment("用户名称")
//    @Column(name = "name", length = 32)
    public String name;

    //    @Comment("真实姓名")
//    @Column(name = "real_name", length = 32)
    public String realName;

    //    @Comment("证件类型")
//    @Column(name = "id_type", length = 4)
    public String idType;

    //    @Comment("证件号码")
//    @Column(name = "id_code", length = 32)
    public String idCode;

    //    @Comment("用户性别")
//    @Column(name = "sex")
    public Integer sex;

    //    @Comment("用户生日")
//    @Column(name = "birthday",length = 8)
    public String birthday;

    //    @Comment("用户邮箱")
//    @Column(name = "email",length = 32)
    public String email;

    //    @Comment("用户电话")
//    @Column(name = "telephone",length = 32)
    public String telephone;

    //    @Comment("用户地址")
//    @Column(name = "address",length = 32)
    public String address;

    //    @Comment("用户登录密码")
//    @Column(name = "password",length = 64)
    public String passWord;

    //0-禁用    1-启用
//    @Comment("用户状态")
//    @Column(name = "enabled",length = 1)
    public String enabled;

    //    @Comment("备注信息")
//    @Column(name = "remark", length = 500)
    public String remark;

    //0-删除状态   1-未删除状态
//    @Comment("删除标记")
//    @Column(name = "delete_state",length = 1)
    public String deleteState;

    //    @Comment("部门")
//    @Column(name = "dept",length = 12)
    public String dept;

    //    @Comment("手机地址")
//    @Column(name = "phone_seq_num",length = 32)
    public String phoneSeqNum;

    //    @Comment("手机类型")
//    @Column(name = "phone_type",length = 1)
    public String phoneType;

    //    @Comment("用户头像")
//    @Column(name = "photo_url",length =128 )
    public String photoUrl;

    //    @Comment("全天提醒时间")
//    @Column(name = "all_day_reminder_time",length =4 )
    public String allDayReminderTime;

    //    @Comment("微信openid")
//    @Column(name = "openId",length =128 )
    public String openId;

    //    @Comment("是否已订阅")
//    @Column(name = "isSubscribe",length =1 )
    public String isSubscribe;//0：未订阅；1：已订阅

    //    @Comment("行业ID")
//    @Column(name = "industryId",length =1 )
    public String industryId;//行业ID

    //    @Comment("职位ID")
//    @Column(name = "positionId",length =1 )
    public String positionId;//职位ID

    //    @Comment("是否引导")
//    @Column(name = "is_guide",length =1 )
    public String isGuide;//0：未引导；1：已引导

    //验证码
//    @Transient
    public String valiCode;
}

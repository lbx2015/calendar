package com.riking.calendar.pojo;

import com.google.gson.Gson;

//@Entity
//@Table(name = "t_app_user")
public class AppUser {
    public String photoUrl;
    //用户表
//	@Id
//	@Column(name = "Id", length = 32)
//	@GenericGenerator(name = "system-uuid", strategy = "uuid")
//	@GeneratedValue(generator = "system-uuid")
    public String id;

    //	@Comment("用户名称")
//	@Column(name = "name", length = 32)
    public String name;

    //	@Comment("真实姓名")
//	@Column(name = "real_name", length = 32)
    public String realName;

    //	@Comment("证件类型")
//	@Column(name = "id_type", length = 4)
    public String idType;

    //	@Comment("证件号码")
//	@Column(name = "id_code", length = 32)
    public String idCode;

    //	@Comment("用户性别")
//	@Column(name = "sex") 1 mail 0 femail
    public int sex = 1;

    //	@Comment("用户生日")
//	@Column(name = "birthday",length = 8)
    public String birthday;

    //	@Comment("用户邮箱")
//	@Column(name = "email",length = 32)
    public String email;

    //	@Comment("用户电话")
//	@Column(name = "telephone",length = 32)
    public String telephone;

    //	@Comment("用户地址")
//	@Column(name = "address",length = 32)
    public String address;

    //	@Comment("用户登录密码")
//	@Column(name = "passWord",length = 64)
    public String passWord;

    //0-禁用    1-启用
//	@Comment("用户状态")
//	@Column(name = "enabled")
    public String enabled;

    //	@Comment("备注信息")
//	@Column(name = "remark", length = 500)
    public String remark;

    //0-删除状态   1-未删除状态
//	@Comment("删除标记")
//	@Column(name = "delete_state")
    public String deleteState;

    //	@Comment("部门")
//	@Column(name = "dept")
    public String dept;

    //	@Comment("手机地址")
//	@Column(name = "phone_seq_num")
    public String phoneSeqNum;

    //	@Comment("手机类型") "1" iphone "2" android
//	@Column(name = "phone_type")
    public String phoneType = "2";
    //验证码
//	@Transient
    public String valiCode;

    @Override
    public String toString() {
        Gson s = new Gson();
        return s.toJson(this);
    }
}

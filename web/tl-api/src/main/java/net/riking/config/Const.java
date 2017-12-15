package net.riking.config;

/**
 * 常量类
 * 
 * @author kai.cheng
 *
 */
public class Const {

	public static final String MODEL_ATTRS_JSON_PATH = "/static/dict/";

	public static final String TASK_RISK_DATA_GROUP = "TASK_RISK_DATA_GROUP";

	public static final String TASK_CUST_GRADE_GROUP = "TASK_CUST_GRADE_GROUP";

	public static final String TASK_FLASH_BACK_GROUP = "TASK_FLASH_BACK_GROUP";

	public static final String EXPORT_REPORT_GROUP = "EXPORT_REPORT_GROUP";

	public static final String TL_STATIC_FLAG_PATH = "/static/images/flag/";

	public static final String TL_STATIC_PATH = "/static";

	public static final String TL_PHOTO_PATH = "/images/user/photo/";

	public static final String TL_FLAG_PATH = "/images/flag/";

	public static final String TL_ABOUT_HTML5_PATH = "/financialDeskAppAbout.html";

	public static final String TL_AGREEMENT_HTML5_PATH = "/agreement.html";

	public static final String TL_REPORT_HTML5_PATH = "/reportListApp.html?id=";

	public static final String TL_REPORT_RICH_TEXT_HTML5_PATH = "/reportRichText.html?id=";

	/* 验证码有效时间 */
	public static final String VALID_ = "VALID_";

	/* 验证码有效时间 5分钟 */
	public static final Integer VALID_CODE_TIME = 60 * 5;

	public static final String CTRY_HDAY_CRCY = "CTRY_HDAY_CRCY";

	/* 所有数据字典 */
	public static final String SYS_DICT = "SYS_DICT";

	/* 默认头像 */
	public static final String DEFAULT_PHOTO_URL = "defaultPhotoUrl.jpg";

	// /* 根据tablename */
	// public static final String SYS_CLAZZ_DICT = "SYS_CLAZZ_DICT";
	// public static final String SYS_FIELD_DICT = "SYS_FIELD_DICT";

	/** -------------------登录方式 begin-------------------------- */
	public static final int LOGIN_REGIST_TYPE_PHONE = 1;// 手机号

	public static final int LOGIN_REGIST_TYPE_WECHAT = 2;// 微信号
	/** --------------------登录方式 end-------------------------- */

	/** -------------------数据是否有效 1有效，0无效 begin-------------------------- */

	public static final int EFFECTIVE = 1;// 有效数据

	public static final int INVALID = 0;// 无效数据

	/** -------------------数据是否有效 1有效，0无效 end-------------------------- */

	/** -------------------男女常量 begin-------------------------- */

	public static final int USER_SEX_MAN = 1;// 男

	public static final int USER_SEX_WOMAN = 0;// 女

	/** --------------------男女常量 end-------------------------- */

	/** -------------------请求方向 begin-------------------------- */

	public static final String DIRECT_UP = "up";

	public static final String DIRECT_DOWN = "down";

	/** --------------------请求方向 end-------------------------- */

	/** -------------------对象类型 begin-------------------------- */

	public static final int OBJ_TYPE_1 = 1;// 问题

	public static final int OBJ_TYPE_2 = 2;// 话题

	public static final int OBJ_TYPE_3 = 3;// 用户

	/** --------------------对象类型 end-------------------------- */

	/** -------------------操作类型 begin-------------------------- */

	public static final int OBJ_OPT_FOLLOW = 0;// 关注

	public static final int OBJ_OPT_GREE = 1;// 点赞

	public static final int OBJ_OPT_COLLECT = 2;// 收藏

	public static final int OBJ_OPT_SHIELD = 3;// 屏蔽

	/** --------------------操作类型 end-------------------------- */

	/** -------------------操作类型 begin-------------------------- */

	public static final int OBJ_TYPE_ANSWER = 1;// 回答

	public static final int OBJ_TYPE_NEWS = 2;// 资讯

	/** --------------------操作类型 end-------------------------- */

	/** -------------------话题操作类型 begin-------------------------- */

	public static final int TOP_OBJ_OPT_ESSENCE = 1;// 精华

	public static final int TOP_OBJ_OPT_QUEST = 2;// 问题

	public static final int TOP_OBJ_OPT_EXRESP = 3;// 优秀回答者

	/** --------------------话题操作类型 end-------------------------- */

	/** -------------------操作类型状态 begin-------------------------- */

	public static final int OPT_TYPE_BLANK_STATUS = 0;// 不显示状态

	public static final int OPT_TYPE_FOLLOW_STATUS = 1;// 显示关注状态

	public static final int OPT_TYPE_INVITE_STATUS = 2;// 显示邀请状态

	public static final int OPT_TYPE_SUBSCRIBE_STATUS = 3;// 显示订阅状态
	/** --------------------操作类型状态 end-------------------------- */

	/** -------------------搜索栏操作对象状态 begin-------------------------- */
	public static final int OPJ_TYPE_REPORT = 1;// 1-报表

	public static final int OPJ_TYPE_TOPIC = 2;// 2-话题

	public static final int OPJ_TYPE_CONTACTS = 3;// 3-人脉

	public static final int OPJ_TYPE_NEWS = 4;// 4-资讯

	public static final int OPJ_TYPE_QUEST = 5;// 5-问题
	/** --------------------搜索栏操作对象状态 end-------------------------- */
}

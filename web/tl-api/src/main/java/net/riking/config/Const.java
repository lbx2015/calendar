package net.riking.config;

import org.apache.activemq.ActiveMQConnection;

/**
 * 常量类
 * 
 * @author kai.cheng
 *
 */
public class Const {

	public static final String SYS_NAME_FLAG = "【悦历】";

	public static final String KEY_WORD = "KEYWORD";

	public static final Integer IS_DELETE = 0;

	public static final Integer IS_NOT_DELETE = 1;

	public static final String EMPTY = null;

	public static final String MODEL_ATTRS_JSON_PATH = "/static/dict/";

	public static final String TASK_RISK_DATA_GROUP = "TASK_RISK_DATA_GROUP";

	public static final String TASK_CUST_GRADE_GROUP = "TASK_CUST_GRADE_GROUP";

	public static final String TASK_FLASH_BACK_GROUP = "TASK_FLASH_BACK_GROUP";

	public static final String EXPORT_REPORT_GROUP = "EXPORT_REPORT_GROUP";

	/**
	 * 图片路径
	 */

	public static final String TL_STATIC_PATH = "/static/";

	public static final String TL_FLAG_PATH = "/images/flag/";

	public static final String TL_STATIC_FLAG_PATH = "/static/images/flag/";

	public static final String TL_PHOTO_PATH = "/images/user/";

	public static final String TL_FEED_BACK_PHOTO_PATH = "/images/feedBack/";

	public static final String TL_NEWS_CONTENT_PATH = "/images/news/content/";

	public static final String TL_NEWS_COVER_PATH = "/images/news/cover/";

	public static final String TL_BANNER_PHOTO_PATH = "/images/banner/";

	public static final String TL_TOPIC_PHOTO_PATH = "/images/topic/";

	public static final String TL_TEMP_PHOTO_PATH = "/images/temp/";

	public static final String TL_QUESTION_PHOTO_PATH = "/images/question/";

	public static final String TL_ANSWER_PHOTO_PATH = "/images/answer/";

	public static final String TL_REPORT_PHOTO_PATH = "/images/report/";

	public static final String TL_ABOUT_HTML5_PATH = "/about.html";

	public static final String TL_POLICY_HTML5_PATH = "/policy.html";

	public static final String TL_REPORT_HTML5_PATH = "/report.html";

	public static final String TL_USER_HTML5_PATH = "/userGrade.html";

	public static final String TL_QUESTIONSHARE_HTML5_PATH = "/questionShare.html";

	public static final String TL_REPORT_INQUIRY_HTML5_PATH = "/inquiry.html";

	public static final String TL_QUESTION_ANSWER_HTML5_PATH = "/answer.html";

	/* 验证码有效时间 */
	public static final String VALID_ = "VALID_";

	/* 验证码有效时间 5分钟 */
	public static final Integer VALID_CODE_TIME = 60 * 5;

	public static final String CTRY_HDAY_CRCY = "CTRY_HDAY_CRCY";

	/* 所有数据字典 */
	public static final String SYS_DICT = "SYS_DICT";

	// 系统日历
	public static final String SYS_DAY = "SYS_DAY_";

	/** -------------------统计数 begin-------------------------- */
	/* 报表类型统计数 */
	public static final String COUNT_BY_REPORT_TYPE = "COUNTBYREPORTTYPE";

	/** -------------------统计数 end-------------------------- */
	/** -------------------审核状态 begin-------------------------- */
	/* 未审核 */
	public static final int ADUIT_NO = 0;

	/* 审核通过 */
	public static final int ADUIT_PASS = 1;

	/* 审核不通过 */
	public static final int ADUIT_NOT_PASS = 2;

	/** -------------------审核状态 end-------------------------- */

	/** -------------------订阅状态 begin-------------------------- */
	/* 取消订阅 */
	public static final int IS_NOT_SUBSCRIBE = 0;

	/* 订阅 */
	public static final int IS_SUBSCRIBE = 1;

	/** -------------------订阅状态 end-------------------------- */

	/** -------------------mq队列 begin-------------------------- */
	/* 系统通知队列 */
	public static final String SYS_INFO_QUEUE = "sysInfoQueue";

	/* 系统操作队列 点赞，收藏，关注 */
	public static final String SYS_OPT_QUEUE = "sysOptQueue";

	/* 系统消息队列 */
	public static final String SYS_MES_QUEUE = "sysMesQueue";

	/** -------------------mq队列 end-------------------------- */
	/** -------------------mq连接信息 begin-------------------------- */
	public static final String MQ_USER_NAME = ActiveMQConnection.DEFAULT_USER;

	public static final String MQ_PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;

	// 默认连接地址
	public static final String MQ_BROKE_URL = ActiveMQConnection.DEFAULT_BROKER_URL;
	/** -------------------mq连接信息 begin-------------------------- */
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

	/** -------------------默认页数 begin-------------------------- */

	public static final int APP_PAGENO_10 = 10;// 默认10页

	public static final int APP_PAGENO_30 = 30;// 默认30页

	public static final int APP_PAGENO_50 = 50;// 默认50页

	/** --------------------男女常量 end-------------------------- */

	/** -------------------请求方向 begin-------------------------- */

	public static final String DIRECT_UP = "up";

	public static final String DIRECT_DOWN = "down";

	/** --------------------请求方向 end-------------------------- */

	/* -------------------对象类型 begin-------------------------- */
	/** 问题 */
	public static final int OBJ_TYPE_1 = 1;

	/** 话题 */
	public static final int OBJ_TYPE_2 = 2;

	/** 用户 */
	public static final int OBJ_TYPE_3 = 3;

	/* --------------------对象类型 end-------------------------- */

	/** -------------------操作类型 begin-------------------------- */

	public static final int OBJ_OPT_FOLLOW = 0;// 关注

	public static final int OBJ_OPT_GREE = 1;// 点赞

	public static final int OBJ_OPT_COLLECT = 2;// 收藏

	public static final int OBJ_OPT_SHIELD = 3;// 屏蔽

	public static final int OBJ_OPT_COMMENT = 1;// 评论

	public static final int OBJ_OPT_ANSWER = 2;// 回答

	public static final int OBJ_OPT_INQUIRY = 3;// 提问

	/** --------------------操作类型 end-------------------------- */

	/** -------------------操作类型 begin-------------------------- */
	/** 回答 */
	public static final int OBJ_TYPE_ANSWER = 1;

	/** 资讯 */
	public static final int OBJ_TYPE_NEWS = 2;

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

	/* -------------------队列类型 begin-------------------------- */
	/* 系统通知 */
	public static final int MQ_SYS_INFO = 0;

	/** 邀请回答的邀请 */
	public static final int MQ_OPT_ANSWERINVITE = 1;

	/** 问题回答点赞或收藏 */
	public static final int MQ_OPT_QA_AGREEOR_COLLECT = 2;

	/** 资讯的收藏 */
	public static final int MQ_OPT_NEW_COLLECT = 3;

	/** 问题的屏蔽 */
	public static final int MQ_OPT_SHIELD_QUEST = 4;

	/** 问题，话题，用户的关注 */
	public static final int MQ_OPT_FOLLOW = 5;

	/** 评论点赞 */
	public static final int MQ_OPT_COMMENT_AGREE = 6;

	/** 通讯录的邀请 */
	public static final int MQ_OPT_CONTACTS_INVITE = 7;

	/** 问题回答的评论 */
	public static final int MQ_OPT_QANSWER_COMMENT = 8;

	/** 资讯的评论发布 */
	public static final int MQ_OPT_NEWS_COMMENT = 9;

	/** 评论的回复和回复的回复 */
	public static final int MQ_OPT_COMMENT_REPLY = 10;

	/** 问题回答 */
	public static final int MQ_OPT_QUESTION_ANWSER = 11;

	/* --------------------队列类型 end-------------------------- */

	/* -------------------消息通知类型 begin-------------------------- */
	// 系统信息
	public static final int NOTICE_SYS_INFO = 0;

	/** 被邀请回答的邀请 */
	public static final int NOTICE_OPT_ANSWERINVITE = 1;

	/** 问题回答被点赞 */
	public static final int NOTICE_OPT_QA_AGREEOR = 2;

	/** 问题回答被收藏 */
	public static final int NOTICE_OPT_QA_COLLECT = 3;

	/** 问题被关注 */
	public static final int NOTICE_OPT_QUESTION_FOLLOW = 4;

	/** 被关注的用户 */
	public static final int NOTICE_OPT_USER_FOLLOW = 5;

	/** 评论被点赞 */
	public static final int NOTICE_OPT_COMMENT_AGREE = 6;

	/** 问题回答的被评论 */
	public static final int NOTICE_OPT_QANSWER_COMMENT = 7;

	/** 评论的回复和回复的被回复 */
	public static final int NOTICE_OPT_COMMENT_REPLY = 8;

	/* --------------------消息通知类型 end-------------------------- */

	// 系统类的通知
	public static final String SYS_NOTICE_FROME_SYS = "SYS";

	public static final String SYS_NOTICE_TO_USER = "ALL";

	/* -------------------系统通知字典标识 begin-------------------------- */
	public static final String SYS_NOTICE_T_SYS_NOTICE = "T_SYS_NOTICE";

	// 用户注册通知
	public static final String SYS_NOTICE_USER_REGISTER = "USER_REGISTER_NOTICE";

	/* -------------------系统通知字典标识 end-------------------------- */
}

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

	public static final Integer VALI_CODE_TIME = 60;

	public static final String CTRY_HDAY_CRCY = "CTRY_HDAY_CRCY";

	/* 所有数据字典 */
	public static final String SYS_DICT = "SYS_DICT";

	// /* 根据tablename */
	// public static final String SYS_CLAZZ_DICT = "SYS_CLAZZ_DICT";
	// public static final String SYS_FIELD_DICT = "SYS_FIELD_DICT";
	/** -------------------数据是否有效 1有效，0无效 begin-------------------------- */

	public static final int EFFECTIVE = 1;// 有效数据

	public static final int INVALID = 0;// 无效数据

	/** -------------------数据是否有效 1有效，0无效 end-------------------------- */

	/** -------------------请求方向 begin-------------------------- */

	public static final String DIRECT_UP = "up";

	public static final String DIRECT_DOWN = "down";

	/** --------------------请求方向 end-------------------------- */

	/** -------------------对象类型 begin-------------------------- */

	public static final int OBJ_TYPE_1 = 1;// 问题

	public static final int OBJ_TYPE_2 = 2;// 话题

	/** --------------------对象类型 end-------------------------- */

}

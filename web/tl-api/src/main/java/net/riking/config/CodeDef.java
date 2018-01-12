package net.riking.config;

public class CodeDef {

	public static final short SUCCESS = 200;

	public static final short ERROR = 500;

	public static final class EMP {
		public static final short NAME_IS_NULL = 1001;

		// 服务器异常
		public static final short GENERAL_ERR = -999;

		public static final String GENERAL_ERR_DESC = "服务器异常";

		// 用户密码错误
		public static final short USER_PASS_ERR = -998;

		public static final String USER_PASS_ERR_DESC = "用户密码错误";

		// 登录超时
		public static final short LOGIN_TIME_OUT = -997;

		public static final String LOGIN_TIME_OUT_DESC = "登录超时";

		// 验证码错误
		public static final short CHECK_CODE_ERR = -996;

		public static final String CHECK_CODE_ERR_DESC = "验证码错误";

		// 验证码超时
		public static final short CHECK_CODE_TIME_OUT = -995;

		public static final String CHECK_CODE_TIME_OUT_DESC = "验证码超时";

		// 数据未找到
		public static final short DATA_NOT_FOUND = -994;

		public static final String DATA_NOT_FOUND_DESC = "数据未找到";

		// 短信通道发送错误
		public static final short SMS_SEND_ERROR = -993;

		public static final String SMS_SEND_ERROR_DESC = "短信通道发送错误";

		// 手机号为空
		public static final short PHONE_NULL_ERROR = -992;

		public static final String PHONE_NULL_ERROR_DESC = "手机号为空";

		// 手机格式错误
		public static final short PHONE_FORM_ERROR = -988;

		public static final String PHONE_FORM_ERROR_DESC = "手机号格式错误";

		// 验证码为空
		public static final short VERIFYCODE_NULL_ERROR = -991;

		public static final String VERIFYCODE_NULL_ERROR_DESC = "验证码为空";

		// 参数错误
		public static final short PARAMS_ERROR = -990;

		public static final String PARAMS_ERROR_DESC = "参数错误";

		// 该用户被禁用
		public static final short USER_INVALID_ERROR = -989;

		public static final String USER_INVALID_ERROR_DESC = "该用户被禁用";

		// 列表查询为空
		public static final short DATE_FOUND_EMPTY = -987;

		public static final String DATE_FOUND_EMPTY_DESC = "没有更多内容了";

		// 签到失败
		public static final short SIGN_ERROR = -986;

		public static final String SIGN_ERROR_DESC = "签到失败";

		// 邮箱发送失败
		public static final short EMAIL_ERROR = -985;

		public static final String EMAIL_ERROR_DESC = "邮箱发送失败";

		// 未到核销时间
		public static final short REPORT_NOTTO_COMPLETEDATE_ERROR = -984;

		public static final String REPORT_NOTTO_COMPLETEDATE_ERROR_DESC = "未到核销时间";

		// 逾期报销不能核销
		public static final short REPORT_EXPIRETASK_ERROR = -983;

		public static final String REPORT_EXPIRETASK_ERROR_DESC = "逾期报销不能核销";

		//参数不能为空
		public static final short PARAM_EMPTY_ERROR = -982;

		public static final String PARAM_EMPTY_ERROR_DESC = "参数不能为空";
		// 报表订阅失败
		public static final short REPORT_SUBSCRIBE_ERROR = -981;

		public static final String REPORT_SUBSCRIBE_ERROR_DESC = "报表订阅失败";
		
		// 含有敏感字
		public static final short REPORT_SHIELD_ERROR = -980;

		public static final String REPORT_SHIELD_ERROR_DESC = "内容含有敏感词";
		
		
		// 含有敏感字
		public static final short REPORT_TOKEN_ERROR = -979;

		public static final String REPORT_TOKEN_ERROR_DESC = "请求异常！";
		
		
	}

}

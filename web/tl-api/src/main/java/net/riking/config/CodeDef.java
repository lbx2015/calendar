package net.riking.config;

public class CodeDef {

	public static final short SUCCESS = 200;
	public static final short ERROR = 500;
	
	public static final class EMP{
		public static final short NAME_IS_NULL = 1001;
		//通用错误
		public static final short GENERAL_ERR = -999;
		public static final String GENERAL_ERR_DESC = "通用错误";
		//用户密码错误
		public static final short USER_PASS_ERR = -998;
		public static final String USER_PASS_ERR_DESC = "用户密码错误";
		//登录超时
		public static final short LOGIN_TIME_OUT = -997;
		public static final String LOGIN_TIME_OUT_DESC = "登录超时";
		//验证码错误
		public static final short CHECK_CODE_ERR = -996;
		public static final String CHECK_CODE_ERR_DESC = "验证码错误";
		//验证码超时
		public static final short CHECK_CODE_TIME_OUT = -995;
		public static final String CHECK_CODE_TIME_OUT_DESC = "验证码超时";
		//数据未找到
		public static final short DATA_NOT_FOUND = -994;
		public static final String DATA_NOT_FOUND_DESC = "数据未找到";
		//短信通道发送错误
		public static final short SMS_SEND_ERROR = -993;
		public static final String SMS_SEND_ERROR_DESC = "短信通道发送错误";
		//手机号为空
		public static final short PHONE_NULL_ERROR = -992;
		public static final String PHONE_NULL_ERROR_DESC = "手机号为空";
		//验证码为空
		public static final short VERIFYCODE_NULL_ERROR = -991;
		public static final String VERIFYCODE_NULL_ERROR_DESC = "验证码为空";
		//参数错误
		public static final short PARAMS_ERROR = -990;
		public static final String PARAMS_ERROR_DESC = "参数错误";
		//该用户被禁用
		public static final short USER_INVALID_ERROR = -989;
		public static final String USER_INVALID_ERROR_DESC = "该用户被禁用";
		
	}
	
}

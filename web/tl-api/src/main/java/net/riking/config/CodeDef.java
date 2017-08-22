package net.riking.config;

public class CodeDef {

	public static final short SUCCESS = 200;
	public static final short ERROR = 500;
	
	public static final class EMP{
		public static final short NAME_IS_NULL = 1001;
		//通用错误
		public static final short GENERAL_ERR = -999;
		//用户密码错误
		public static final short USER_PASS_ERR = -998;
		//登录超时
		public static final short LOGIN_TIME_OUT = -997;
	}
}

package cn.wurock.blog.common.constant;

public class ResultCodeConstant {
	// 成功
	public static final Integer SUCCESS = 200;
	// 失败
	public static final Integer FAIL = 5001;
	// 未授权
	public static final Integer UNAUTHORIZED = 401;
	
	// 用户登录相关错误码
	public static final Integer USER_NOT_LOGIN = 1001;
	// 用户注册相关错误码
	public static final Integer USER_REGISTER_ERROR = 1002;
	// 用户其它错误码
	public static final Integer USER_OTHER_ERROR = 1003;
	
	// 文件上传相关错误码
	public static final Integer FILE_ERROR = 2001;

}

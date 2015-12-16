package com.ncu.testbank.base.exception;

public class ErrorCode {
	public static final ErrorCode LOGIN_SUCCESS = new ErrorCode(10000, "登录成功");
	public static final ErrorCode PASSWORD_ERROR = new ErrorCode(10001,"用户名或密码错误");
	public static final ErrorCode LOGOUT_SUCCESS = new ErrorCode(10010, "登出成功");
	public static final ErrorCode LOGOUT_FAIL = new ErrorCode(10011,"登出系统失败");
	public static final ErrorCode USER_ROLE_IS_NULL = new ErrorCode(10023,"登录失败，用户角色未授权");
	public static final ErrorCode USERNAME_MISSING = new ErrorCode(10043,"用户名不存在，请重试！");
	public static final ErrorCode USERINFO_MISSING = new ErrorCode(10050, "用户信息丢失，请重新登录！");
	public static final ErrorCode USER_UNLOGIN = new ErrorCode(10088, "用户未登录！");
	
	public static final ErrorCode REAUTHEN_SUCCESS = new ErrorCode(20000, "二级认证成功");
	public static final ErrorCode TOKEN_INVALID = new ErrorCode(20001, "用户登录失效，请重新登录！");
	public static final ErrorCode REAUTHEN_FAIL = new ErrorCode(20002, "二级认证密码错误，请重新认证！");
	
	
	public static final ErrorCode FILE_TYPE_ERROR = new ErrorCode(50001, "请上传csv格式的文件！");
	public static final ErrorCode FILE_IO_ERROR = new ErrorCode(50002, "文件IO出错，请检查文件后重试或联系管理人员！");
	public static final ErrorCode FILE_PROPERTIES_ERROR = new ErrorCode(50003, "配置文件出错，请联系管理人员！");
	
	public static final ErrorCode MAP_CONVERT_ERROR = new ErrorCode(40001, "数据转换Map出错，联系管理人员！");
	
	public static final ErrorCode CALL_SUCCESS = new ErrorCode(0, "操作成功");
	public static final ErrorCode ERROR_HTTP_REQUEST_IS_NOT_AVAILABLE = new ErrorCode(90001,"HTTP请求错误"); 
	public static final ErrorCode ERROR_REMOTE_SERVER_RETURN_ERROR = new ErrorCode(90002, "远程服务返回错误"); 
	public static final ErrorCode ERROR_RESOURCE_IS_NOT_READY = new ErrorCode(90003, "系统资源缺失"); 
	public static final ErrorCode ERROR_TECHNICAL_ERROR = new ErrorCode(99999, "技术处理失败"); 
	public static final ErrorCode ERROR_CONNECTION_TIMEOUT = new ErrorCode(99998, "连接超时"); 
	public static final ErrorCode ERROR_INVALID_AUTHORIZATION = new ErrorCode(99997, "无授权操作"); 
	public static final ErrorCode ERROR_NOT_SUPPORT_REMOTE_CALL = new ErrorCode(99996, "不支持远程系统调用"); 
	public static final ErrorCode ERROR_DATABASE_TECH_EXCEPTION = new ErrorCode(80001,"数据库操作异常"); 
	public static final ErrorCode ERROR_FUNCTION_NOT_SUPPORT = new ErrorCode(99995, "该功能点不支持");
	public static final ErrorCode ERROR_DUMPLICATE_PRIMARY_KEY = new ErrorCode(80002, "数据记录已经存在"); 
	public static final ErrorCode ERROR_RECORD_NOT_EXISTS = new ErrorCode(80003, "数据记录不存在");
	public static final ErrorCode ERROR_DATA_IS_MALFORM = new ErrorCode(80004, "数据格式不符合要求");
	public static final ErrorCode ERROR_ILLEGAL_ACCESS = new ErrorCode(80005, "非法用户访问");
	public static final ErrorCode ERROR_ILLEGAL_OPERATION = new ErrorCode(80006, "没有操作权限，请找管理员确认权限配置!");

	public int code;
	public String name;

	public ErrorCode(int code, String name) {
		this.code = code;
		this.name = name;
	}

	public String getName(int code) {
		return name;
	}
}

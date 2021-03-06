package com.ncu.testbank.base.exception;

import org.apache.shiro.authc.AuthenticationException;

public class ShiroException extends AuthenticationException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2996860414512658725L;

	public ShiroException(ErrorCode msg) {
		super(msg.name);
		this.errorCode = msg;
	}

	public ShiroException(Throwable cause) {
		super(cause);
	}

	public ShiroException(ErrorCode msg, Throwable cause) {
		super(msg.name, cause);
		this.errorCode = msg;
	}

	private ErrorCode errorCode;

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		if (super.getCause() == null) {
			return super.getMessage();
		} else {
			return super.getMessage() + ", nested exception is "
					+ super.getCause().toString();
		}
	}
}

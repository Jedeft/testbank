package com.ncu.testbank.base.exception;


public class ServiceException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6727428665488862786L;
	
	public ServiceException(ErrorCode msg) {
		super(msg.name);
		this.errorCode = msg;
	}
	
	public ServiceException(Throwable cause) {
		super(cause);
	}
	
	public ServiceException(ErrorCode msg, Throwable cause) {
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
		if(super.getCause()==null) {
			return super.getMessage();
		}else {
			return super.getMessage() + ", nested exception is " + super.getCause().toString();
		}
	}
}

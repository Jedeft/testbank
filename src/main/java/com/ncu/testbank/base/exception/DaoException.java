package com.ncu.testbank.base.exception;

public class DaoException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 332655721777194415L;
	
	private ErrorCode errorCode = null;
	
	public DaoException(ErrorCode error) {
		super(error.name);
		errorCode = error;
	}
	
	public DaoException(Throwable cause) {
		super(cause);
	}
	
	public DaoException(ErrorCode error, Throwable cause) {
		super(error.name, cause);
		errorCode = error;
	}
	
	
	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public String getMessage() {
		if(super.getCause()==null) {
			return super.getMessage();
		}else {
			return super.getMessage() + ", nested exception is " + super.getCause().toString();
		}
	}
}

package com.imc.sqlclient.exception;

public class SQLClientException extends RuntimeException  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SQLClientException(String message) {
		super(message);
	}
	
	public SQLClientException(Throwable throwable) {
		super(throwable);
	}

}

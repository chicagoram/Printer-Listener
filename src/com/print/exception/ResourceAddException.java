package com.print.exception;

public class ResourceAddException extends RecoverableException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ResourceAddException(String message, Throwable t) {
		super(message, t);

	}

	public ResourceAddException(String message) {
		super(message);

	}

}

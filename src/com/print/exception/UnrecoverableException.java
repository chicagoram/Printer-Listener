package com.print.exception;

/*
 *
 * @author Ram Narasimhan
 * 
 *
 * Copyright (c) 2003 Boise Office Solutions
 * 800 W. Bryn Marw Ave, Itasca, IL 60015
 * All Rights Reserved.
 *
 */

import java.io.PrintWriter;

public class UnrecoverableException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Throwable baseThrowable = null;

	public UnrecoverableException(String message, Throwable t) {
		super(message);
		baseThrowable = t;

	}

	public UnrecoverableException(String message) {

		super(message);

	}

	public Throwable getRootCause() {
		return baseThrowable;
	}

	@Override
	public String getMessage() {
		if (baseThrowable == null) {
			return super.getMessage();
		} else {
			return super.getMessage() + "; nested exception is:  \n\t"
					+ baseThrowable.toString();
		}
	}

	@Override
	public void printStackTrace(PrintWriter s) {
		super.printStackTrace(s);
		if (baseThrowable != null) {
			s.print("\n\t Underlying Throwable stack trace follows : \n\t");
			baseThrowable.printStackTrace(s);
		}
	}

	@Override
	public void printStackTrace() {
		this.printStackTrace(System.err);
	}

}

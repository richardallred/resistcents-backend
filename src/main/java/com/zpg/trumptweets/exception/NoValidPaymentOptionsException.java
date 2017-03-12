package com.zpg.trumptweets.exception;

public class NoValidPaymentOptionsException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2107505630995594910L;
	
	public NoValidPaymentOptionsException(String message){
		super(message);
	}

}

package com.zpg.trumptweets.exception;

public class InvalidUserException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1445392914083091462L;
	
	public InvalidUserException(String message){
		super(message);
	}

}

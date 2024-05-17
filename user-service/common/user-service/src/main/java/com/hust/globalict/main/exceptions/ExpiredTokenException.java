package com.hust.globalict.main.exceptions;

@SuppressWarnings("serial")
public class ExpiredTokenException extends Exception {
	public ExpiredTokenException(String message) {
		super(message);
	}
}

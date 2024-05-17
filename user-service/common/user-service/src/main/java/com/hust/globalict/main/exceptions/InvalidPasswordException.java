package com.hust.globalict.main.exceptions;

@SuppressWarnings("serial")
public class InvalidPasswordException extends Exception{
    public InvalidPasswordException(String message) {
        super(message);
    }
}
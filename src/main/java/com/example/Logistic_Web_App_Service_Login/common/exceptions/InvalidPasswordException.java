package com.example.Logistic_Web_App_Service_Login.common.exceptions;

@SuppressWarnings("serial")
public class InvalidPasswordException extends Exception{
    public InvalidPasswordException(String message) {
        super(message);
    }
}
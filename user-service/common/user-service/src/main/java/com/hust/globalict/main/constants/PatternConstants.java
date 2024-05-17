package com.hust.globalict.main.constants;

public class PatternConstants {
    public static final String SPECIAL_CHARACTERS = "@#$%&";

    public static final String USER_LOGIN_PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*[@#$%^&+=])(?!.*\\s).{6,20}$";
    public static final String USER_LOGIN_USERNAME_PATTERN = "^(?=.*[@])([a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})|([0-9]{10})$";
}

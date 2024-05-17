package com.hust.globalict.main.validations.userlogin;

import com.hust.globalict.main.constants.PatternConstants;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidUsernameValidator implements ConstraintValidator<ValidUsername, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return value.matches(PatternConstants.USER_LOGIN_USERNAME_PATTERN);
    }
}

package com.hust.globalict.main.validations.userlogin;

import com.hust.globalict.main.utils.MessageKeyValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidPasswordValidation.class)
public @interface ValidPassword {
    String message() default MessageKeyValidation.USER_LOGIN_PASSWORD_PATTERN;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

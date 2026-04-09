package com.example.asset_manager.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = NoJapaneseCharactersValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface NoJapaneseCharacters {
    String message() default "Japanese characters are not allowed. Use English letters, numbers, spaces, and standard symbols only.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

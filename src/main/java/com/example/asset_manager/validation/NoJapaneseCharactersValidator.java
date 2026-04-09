package com.example.asset_manager.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class NoJapaneseCharactersValidator implements ConstraintValidator<NoJapaneseCharacters, String> {
    private static final Pattern JAPANESE_PATTERN = Pattern.compile("[\\p{IsHan}\\p{IsHiragana}\\p{IsKatakana}\\uFF66-\\uFF9D]");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }

        return !JAPANESE_PATTERN.matcher(value).find();
    }
}

package com.myapt.global.error.validator;

import com.myapt.global.error.annotations.AllowedSortValues;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class AllowedValuesValidator implements ConstraintValidator<AllowedSortValues, String> {

    private Set<String> allowedSet;
    private boolean ignoreCase;

    private boolean nullable;

    @Override
    public void initialize(AllowedSortValues annotation) {
        this.ignoreCase = annotation.ignoreCase();
        this.nullable = annotation.nullable();

        // 허용 리스트를 Set으로 변환하여 대소문자 비교 방식 적용
        if (ignoreCase) {
            this.allowedSet = Arrays.stream(annotation.allowed())
                    .map(String::toLowerCase)
                    .collect(Collectors.toSet());
        } else {
            this.allowedSet = Arrays.stream(annotation.allowed())
                    .collect(Collectors.toSet());
        }
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return nullable; // null 비허용
        }

        String compare = ignoreCase ? value.toLowerCase() : value;

        return allowedSet.contains(compare);
    }
}

package com.myapt.global.error.annotations;

import com.myapt.global.error.validator.AllowedValuesValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {AllowedValuesValidator.class})
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface AllowedSortValues {

    // 메시지 템플릿 (커스텀 플레이스홀더 포함)
    String message() default "{sort.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    // 허용된 값 목록
    String[] allowed() default {};

    // 대소문자 무시 여부
    boolean ignoreCase() default true;
}

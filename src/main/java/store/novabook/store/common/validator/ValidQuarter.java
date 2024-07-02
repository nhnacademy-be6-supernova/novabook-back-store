package store.novabook.store.common.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = QuarterValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidQuarter {
	String message() default "유효하지 않은 분기 형식입니다. 올바른 형식: YYYYQn (e.g., 2023Q1)";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}

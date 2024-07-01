package store.novabook.store.common.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class QuarterValidator implements ConstraintValidator<ValidQuarter, String> {
	private static final String QUARTER_PATTERN = "\\d{4}Q[1-4]";

	@Override
	public boolean isValid(String quarter, ConstraintValidatorContext context) {
		if (quarter == null) {
			return true; // @NotBlank로 null 체크
		}
		return quarter.matches(QUARTER_PATTERN);
	}
}

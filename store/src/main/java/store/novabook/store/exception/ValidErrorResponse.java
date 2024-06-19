package store.novabook.store.exception;

import static java.util.stream.Collectors.*;

import java.util.Map;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

public record ValidErrorResponse(Map<String, String> result) {
	public static ValidErrorResponse from(MethodArgumentNotValidException exception) {
		Map<String, String> result = exception.getBindingResult()
			.getFieldErrors()
			.stream()
			.collect(toMap(FieldError::getField, ValidErrorResponse::getFieldErrorMessage));
		return new ValidErrorResponse(result);
	}

	private static String getFieldErrorMessage(FieldError error) {
		String message = error.getDefaultMessage();
		if (message == null) {
			return "잘못된 요청입니다.";
		}
		return message;
	}
}

package store.novabook.store.common.response;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

@Getter
public class ErrorResponse<T> {

	private final Map<String, Object> header = new HashMap<>();
	private final T body;

	public ErrorResponse(String error, T body) {
		this.header.put("result", "fail");
		this.header.put("message", error);
		this.body = body;
	}

	public static ErrorResponse from(ProblemDetail problemDetail) {
		return new ErrorResponse(ErrorCode.PROBLEM_DETAIL, problemDetail.getDetail());
	}

	public static ErrorResponse from(ValidErrorResponse validErrorResponse) {
		return new ErrorResponse(ErrorCode.INVALID_REQUEST_ARGUMENT, validErrorResponse.message());
	}
}

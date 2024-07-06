package store.novabook.store.common.exception;

import store.novabook.store.common.response.ErrorBody;
import store.novabook.store.common.response.ErrorResponse;

public class FeignClientException extends RuntimeException {
	// private final int status;
	// private final ErrorResponse<ErrorBody> errorResponse;
	//
	// public FeignClientException(int status, ErrorResponse<ErrorBody> errorResponse) {
	// 	super(errorResponse.getHeader().get("resultMessage").toString()); // 오류 메시지를 설정
	// 	this.status = status;
	// 	this.errorResponse = errorResponse;
	// }
	//
	// public int getStatus() {
	// 	return status;
	// }
	//
	// public ErrorResponse<ErrorBody> getErrorResponse() {
	// 	return errorResponse;
	// }
}

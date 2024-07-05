package store.novabook.store.common.exception;

import store.novabook.store.common.response.CouponErrorBody;
import store.novabook.store.common.response.ErrorResponse;

public class FeignClientException extends RuntimeException {
	private final int status;
	private final ErrorResponse<CouponErrorBody> errorResponse;

	public FeignClientException(int status, ErrorResponse<CouponErrorBody> errorResponse) {
		super(errorResponse.getHeader().get("resultMessage").toString()); // 오류 메시지를 설정
		this.status = status;
		this.errorResponse = errorResponse;
	}

	public int getStatus() {
		return status;
	}

	public ErrorResponse<CouponErrorBody> getErrorResponse() {
		return errorResponse;
	}
}

package store.novabook.store.common.exception;

/**
 * 다양한 오류 상태를 나타내는 열거형 클래스입니다. 각 오류 상태는 HTTP 상태 코드와 관련된 메시지를 포함합니다.
 * 이 오류 코드는 Nova Book Store 애플리케이션 내에서 예외 처리에 사용됩니다.
 *
 * <p> 각 오류 코드는 다음과 같이 정의됩니다:
 * <ul>
 *     <li>400 - 잘못된 요청 오류</li>
 *     <li>404 - 리소스를 찾을 수 없음</li>
 *     <li>500 - 내부 서버 오류</li>
 * </ul>
 * </p>
 *
 * <p> 예제 사용법:
 * <pre>
 *     throw new BadRequestException(ErrorCode.INVALID_REQUEST_ARGUMENT);
 * </pre>
 * </p>
 */
public enum ErrorCode {

	// 400
	INVALID_REQUEST_ARGUMENT("잘못된 요청입니다."),
	INVALID_ARGUMENT_TYPE("유효하지 않은 타입입니다."),
	LIMITED_ADDRESS_OVER("주소는 10개까지만 등록 가능합니다."),
	REVIEW_ALREADY_EXISTS("해당 도서에 대한 리뷰가 이미 존재합니다."),
	ORDER_BOOK_ALREADY_EXISTS("주문건에 대한 해당 도서가 이미 존재합니다."),
	DUPLICATED_LOGIN_ID("중복된 아이디입니다."),
	NOT_DELETABLE_CATEGORY("해당 카테고리 등록된 도서가 있어 삭제할 수 없습니다."),

	// 401 로그인 안됨
	UNAUTHORIZED("인증되지 않은 사용자입니다."),

	// 403
	NOT_ENOUGH_PERMISSION("해당 권한이 없습니다."),
	FORBIDDEN("접근 권한이 없습니다."),

	// 404
	MEMBER_NOT_FOUND("해당 회원이 존재하지 않습니다."),
	MEMBER_ADDRESS_NOT_FOUND("해당 회원의 주소가 존재하지 않습니다."),
	MEMBER_STATUS_NOT_FOUND("해당 상태는 존재하지 않습니다. "),
	MEMBER_GRADE_POLICY_NOT_FOUND("해당 등급은 존재하지 않습니다. "),
	MEMBER_GRADE_HISTORY_NOT_FOUND("해당 등급 내역은 존재하지 않습니다. "),
	PAYMENT_NOT_FOUND("해당 결제 방법이 존재하지 않습니다."),
	POINT_HISTORY_NOT_FOUND("해당 포인트 내역이 존재하지 않습니다. "),
	POINT_POLICY_NOT_FOUND("해당 포인트 정책이 존재하지 않습니다. "),
	ORDER_BOOK_NOT_FOUND("해당 주문도서가 존재하지 않습니다. "),
	REVIEW_NOT_FOUND("해당 리뷰가 존재하지 않습니다."),
	BOOK_NOT_FOUND("해당 도서가 존재하지 않습니다"),
	BOOK_STATUS_NOT_FOUND("해당 도서상태가 존재하지 않습니다"),
	CART_NOT_FOUND("해당 장바구니가 존재하지 않습니다"),
	CART_BOOK_NOT_FOUND("해당 장바구니에 도서가 존재하지 않습니다"),
	CATEGORY_NOT_FOUND("해당 카테고리가 존재하지 않습니다"),
	IMAGE_NOT_FOUND("해당 이미지가 존재하지 않습니다"),
	RETURN_POLICY_NOT_FOUND("환불정책 존재하지 않습니다"),
	WRAPPING_PAPER_NOT_FOUND("해당 포장지가 존재하지 않습니다"),
	DELIVERY_FEE_NOT_FOUND("배송료가 존재하지 않습니다"),
	ORDERS_NOT_FOUND("해당 주문건이 존재하지 않습니다"),
	ORDERS_BOOK_NOT_FOUND("주문건에 해당 도서가 존재하지 않습니다"),
	ORDERS_STATUS_NOT_FOUND("주문상태가 존재하지 않습니다"),
	TAG_NOT_FOUND("해당 태그가 존재하지 않습니다"),

	// 409
	DUPLICATED_VALUE("중복된 값입니다."),

	// 500
	INTERNAL_SERVER_ERROR("서버 내부에 문제가 발생했습니다."),
	FAILED_CREATE_BOOK("도서 저장에 실패해였습니다."),
	PROBLEM_DETAIL("문제 발생!"),
	;

	private final String message;

	/**
	 * 주어진 메시지를 사용하여 새로운 {@code ErrorCode}를 생성합니다.
	 *
	 * @param message 오류 메시지
	 */
	ErrorCode(String message) {
		this.message = message;
	}

	/**
	 * 오류 메시지를 반환합니다.
	 *
	 * @return 오류 메시지
	 */
	public String getMessage() {
		return message;
	}
}

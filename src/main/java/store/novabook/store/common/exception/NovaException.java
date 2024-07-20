package store.novabook.store.common.exception;

/**
 * 수수수수퍼노바 쿠폰 api에서 발생하는 모든 예외의 기본 추상 클래스입니다.
 * 이 클래스는 특정 오류 코드를 포함하며, {@link RuntimeException}을 상속합니다.
 */
public abstract class NovaException extends RuntimeException {

	private final ErrorCode errorCode;

	/**
	 * 주어진 오류 코드를 사용하여 {@code NovaException}을 생성합니다.
	 *
	 * @param errorCode 예외와 연관된 오류 코드
	 */
	protected NovaException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}

	/**
	 * 주어진 오류 코드와 원인으로 {@code NovaException}을 생성합니다.
	 *
	 * @param errorCode 예외와 연관된 오류 코드
	 * @param cause     예외의 원인
	 */
	protected NovaException(ErrorCode errorCode, Throwable cause) {
		super(errorCode.getMessage(), cause);
		this.errorCode = errorCode;
	}

	/**
	 * 주어진 오류 코드와 메시지로 {@code NovaException}을 생성합니다.
	 *
	 * @param errorCode 예외와 연관된 오류 코드
	 * @param message   예외 메시지
	 */
	protected NovaException(ErrorCode errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	/**
	 * 예외와 연관된 오류 코드를 반환합니다.
	 *
	 * @return 오류 코드
	 */
	public ErrorCode getErrorCode() {
		return errorCode;
	}
}

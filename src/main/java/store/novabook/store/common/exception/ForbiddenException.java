package store.novabook.store.common.exception;

/**
 * {@code ForbiddenException} 클래스는 접근이 금지된 경우 발생하는 예외를 나타냅니다.
 * 이 예외는 권한 부족 등의 이유로 접근이 허용되지 않을 때 사용됩니다.
 */
public class ForbiddenException extends NovaException {

	/**
	 * 주어진 {@code ErrorCode}로 {@code ForbiddenException}을 생성합니다.
	 *
	 * @param errorCode 접근이 금지된 이유를 나타내는 오류 코드
	 */
	public ForbiddenException(ErrorCode errorCode) {
		super(errorCode);
	}
}

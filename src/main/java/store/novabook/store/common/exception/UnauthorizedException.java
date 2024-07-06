package store.novabook.store.common.exception;

/**
 * 인증되지 않은 요청에 대한 예외 클래스.
 */
public class UnauthorizedException extends NovaException {

	/**
	 * UnauthorizedException 생성자.
	 *
	 * @param errorCode 에러 코드
	 */
	public UnauthorizedException(ErrorCode errorCode) {
		super(errorCode);
	}
}

package store.novabook.store.common.exception;

/**
 * 리소스를 찾을 수 없을 때 발생하는 예외입니다.
 * 이 예외는 {@link NovaException}을 상속하며, 노바 프로젝트 내에서 리소스가 존재하지 않을 경우 던져집니다.
 */
public class NotFoundException extends NovaException {

	/**
	 * 주어진 오류 코드를 사용하여 {@code NotFoundException}을 생성합니다.
	 *
	 * @param errorCode 예외와 연관된 오류 코드
	 */
	public NotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}
}

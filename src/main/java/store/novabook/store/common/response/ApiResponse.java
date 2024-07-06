package store.novabook.store.common.response;

import java.util.HashMap;
import java.util.Map;

/**
 * {@code ApiResponse} 클래스는 API 응답을 표준 형식으로 감싸기 위한 클래스입니다.
 * 응답의 헤더와 본문을 포함합니다.
 *
 * @param <T> 응답 본문의 타입
 */
public class ApiResponse<T> {

	private final Map<String, Object> header = new HashMap<>();
	private final T body;

	/**
	 * {@code ApiResponse} 생성자는 응답의 헤더와 본문을 초기화합니다.
	 *
	 * @param resultMessage 결과 메시지
	 * @param isSuccessful  성공 여부
	 * @param body          응답 본문
	 */
	public ApiResponse(String resultMessage, boolean isSuccessful, T body) {
		this.header.put("resultMessage", resultMessage);
		this.header.put("isSuccessful", isSuccessful);
		this.body = body;
	}

	/**
	 * 성공 응답을 생성합니다.
	 *
	 * @param body 응답 본문
	 * @param <T>  응답 본문의 타입
	 * @return 성공 응답을 감싼 {@code ApiResponse} 객체
	 */
	public static <T> ApiResponse<T> success(T body) {
		return new ApiResponse<>("SUCCESS", true, body);
	}

	/**
	 * 오류 응답을 생성합니다.
	 *
	 * @param body 응답 본문
	 * @param <T>  응답 본문의 타입
	 * @return 오류 응답을 감싼 {@code ApiResponse} 객체
	 */
	public static <T> ApiResponse<T> error(T body) {
		return new ApiResponse<>("FAIL", false, body);
	}

	/**
	 * 응답의 헤더를 반환합니다.
	 *
	 * @return 응답 헤더
	 */
	public Map<String, Object> getHeader() {
		return header;
	}

	/**
	 * 응답 본문을 반환합니다.
	 *
	 * @return 응답 본문
	 */
	public T getBody() {
		return body;
	}
}

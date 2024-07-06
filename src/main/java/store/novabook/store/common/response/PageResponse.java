package store.novabook.store.common.response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * 페이지 응답 DTO 클래스.
 * 페이징된 데이터를 포함한 응답을 나타냅니다.
 *
 * @param <T> 페이지 내의 데이터 타입
 */
@Setter
@Getter
public class PageResponse<T> {

	private Map<String, Object> header = new HashMap<>();
	private int pageNum;
	private int pageSize;
	private long totalCount;
	private List<T> data;

	/**
	 * PageResponse 생성자.
	 *
	 * @param pageNum    페이지 번호
	 * @param pageSize   페이지 크기
	 * @param totalCount 전체 데이터 개수
	 * @param data       페이지 내의 데이터 리스트
	 */
	@JsonCreator
	public PageResponse(@JsonProperty("pageNum") int pageNum, @JsonProperty("pageSize") int pageSize,
		@JsonProperty("totalCount") long totalCount, @JsonProperty("data") List<T> data) {
		this.pageNum = pageNum;
		this.pageSize = pageSize;
		this.totalCount = totalCount;
		this.data = data;
	}

	/**
	 * 성공적인 페이지 응답을 생성합니다.
	 *
	 * @param pageNum    페이지 번호
	 * @param pageSize   페이지 크기
	 * @param totalCount 전체 데이터 개수
	 * @param data       페이지 내의 데이터 리스트
	 * @param <T>        데이터 타입
	 * @return 성공적인 페이지 응답 객체
	 */
	public static <T> PageResponse<T> success(int pageNum, int pageSize, long totalCount, List<T> data) {
		PageResponse<T> response = new PageResponse<>(pageNum, pageSize, totalCount, data);
		response.header.put("resultMessage", "SUCCESS");
		response.header.put("isSuccessful", true);
		return response;
	}

	/**
	 * 헤더에 새로운 키-값 쌍을 추가합니다.
	 *
	 * @param key   헤더 키
	 * @param value 헤더 값
	 */
	public void addHeader(String key, Object value) {
		this.header.put(key, value);
	}

	public Page<T> toPage() {
		Pageable pageable = PageRequest.of(this.pageNum, this.pageSize);
		return new PageImpl<>(this.data, pageable, this.totalCount);
	}
}

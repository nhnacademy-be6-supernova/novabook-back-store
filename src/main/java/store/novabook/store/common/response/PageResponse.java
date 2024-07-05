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

@Setter
@Getter
public class PageResponse<T> {

	private Map<String, Object> header = new HashMap<>();
	private int pageNum;
	private int pageSize;
	private long totalCount;
	private List<T> data;

	@JsonCreator
	public PageResponse(@JsonProperty("pageNum") int pageNum, @JsonProperty("pageSize") int pageSize,
		@JsonProperty("totalCount") long totalCount, @JsonProperty("data") List<T> data) {
		this.pageNum = pageNum;
		this.pageSize = pageSize;
		this.totalCount = totalCount;
		this.data = data;
	}

	public static <T> PageResponse<T> success(int pageNum, int pageSize, long totalCount, List<T> data) {
		PageResponse<T> response = new PageResponse<>(pageNum, pageSize, totalCount, data);
		response.header.put("resultMessage", "SUCCESS");
		response.header.put("isSuccessful", true);
		return response;
	}

	public void addHeader(String key, Object value) {
		this.header.put(key, value);
	}

	public int getTotalPageCount() {
		long result = this.totalCount / this.pageSize;

		if (this.totalCount % this.pageSize != 0) {
			result += 1L;
		}

		return (int)result;
	}

	public Page<T> toPage() {
		Pageable pageable = PageRequest.of(this.pageNum, this.pageSize);
		return new PageImpl<>(this.data, pageable, this.totalCount);
	}
}

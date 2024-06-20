package store.novabook.store.common.response;

import java.util.List;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.Setter;

// 공통 헤더
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "header", "body" })
public class ApiResponse<T> {
	@JsonProperty("header")
	private Header header;

	@JsonProperty("body")
	private T body;

	// 기존 생성자
	public ApiResponse(Header header, T body) {
		this.header = header;
		this.body = body;
	}

	// 디폴트 생성자
	public ApiResponse(T body) {
		this.header = new Header(0, "SUCCESS", true);
		this.body = body;
	}

	@Getter
	@Setter
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonPropertyOrder({ "resultCode", "resultMessage", "isSuccessful" })
	public static class Header {
		@JsonProperty("resultCode")
		private int resultCode;

		@JsonProperty("resultMessage")
		private String resultMessage;

		@JsonProperty("isSuccessful")
		private boolean isSuccessful;

		public Header(int resultCode, String resultMessage, boolean isSuccessful) {
			this.resultCode = resultCode;
			this.resultMessage = resultMessage;
			this.isSuccessful = isSuccessful;
		}
	}

	@Getter
	@Setter
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonPropertyOrder({ "pageNum", "pageSize", "totalCount", "data" })
	public static class PageBody<T> {
		@JsonProperty("pageNum")
		private int pageNum;

		@JsonProperty("pageSize")
		private int pageSize;

		@JsonProperty("totalCount")
		private long totalCount;

		@JsonProperty("data")
		private List<T> data;

		public PageBody(int pageNum, int pageSize, long totalCount, List<T> data) {
			this.pageNum = pageNum;
			this.pageSize = pageSize;
			this.totalCount = totalCount;
			this.data = data;
		}
	}

	@Getter
	@Setter
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonPropertyOrder({ "data" })
	public static class Body<T> {
		@JsonProperty("data")
		private List<T> data;

		public Body(List<T> data) {
			this.data = data;
		}
	}

	public static <T> ApiResponse<PageBody<T>> fromPage(Page<T> page) {
		Header header = new Header(0, "SUCCESS", true);
		PageBody<T> body = new PageBody<>(
			page.getNumber() + 1,
			page.getSize(),
			page.getTotalElements(),
			page.getContent()
		);
		return new ApiResponse<>(header, body);
	}
}

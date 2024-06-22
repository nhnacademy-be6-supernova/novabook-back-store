package store.novabook.store.common.response;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApiResponse<T> {

	private Map<String, Object> header;
	private T body;

	@JsonCreator
	public ApiResponse(@JsonProperty("resultMessage") String resultMessage,
		@JsonProperty("isSuccessful") boolean isSuccessful, @JsonProperty("body") T body) {
		this.header = new HashMap<>();
		this.header.put("resultMessage", resultMessage);
		this.header.put("isSuccessful", isSuccessful);
		this.body = body;
	}

	public static <T> ApiResponse<T> success(T body) {
		return new ApiResponse<>("SUCCESS", true, body);
	}

	public void addHeader(String key, Object value) {
		this.header.put(key, value);
	}
}
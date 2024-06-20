package store.novabook.store.common.handler;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApiResponse<T> {

	private Map<String, Object> header = new HashMap<>();
	private T body;

	public ApiResponse(String resultMessage, boolean isSuccessful, T body) {
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
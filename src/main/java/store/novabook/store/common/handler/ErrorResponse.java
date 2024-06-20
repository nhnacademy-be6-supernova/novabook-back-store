package store.novabook.store.common.handler;

import java.util.HashMap;
import java.util.Map;

public class ErrorResponse<T> {

	private Map<String, Object> header = new HashMap<>();
	private T body;

	public ErrorResponse(String error, T body) {
		this.header.put("result", "fail");
		this.body = body;
	}

	public Map<String, Object> getHeader() {
		return header;
	}

	public T getBody() {
		return body;
	}
}
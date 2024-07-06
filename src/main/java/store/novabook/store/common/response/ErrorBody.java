package store.novabook.store.common.response;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class ErrorBody {

	private String errorCode;
	private String message;
	private Map<String, Object> result;

	@JsonCreator
	public ErrorBody(@JsonProperty("errorCode") String errorCode, @JsonProperty("message") String message,
		@JsonProperty("result") Map<String, Object> result) {
		this.errorCode = errorCode;
		this.message = message;
		this.result = result;
	}
}
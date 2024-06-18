package store.novabook.handler.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatusCode;

public record ErrorStatus(String message, int status, LocalDateTime dateTime) {
	public HttpStatusCode toHttpStatus() {
		return HttpStatusCode.valueOf(status);
	}
}

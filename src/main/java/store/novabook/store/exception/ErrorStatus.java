package store.novabook.store.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatusCode;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorStatus {
	private String message;
	private int status;

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime timestamp;

	public static ErrorStatus from(String title, int status, LocalDateTime timestamp) {
		return ErrorStatus.builder()
			.message(title)
			.status(status)
			.timestamp(timestamp)
			.build();
	}

	public HttpStatusCode toHttpStatus() {
		return HttpStatusCode.valueOf(this.status);
	}
}

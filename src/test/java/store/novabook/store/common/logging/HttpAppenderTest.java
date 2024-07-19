package store.novabook.store.common.logging;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.classic.spi.ILoggingEvent;
import okhttp3.*;

class HttpAppenderTest {

	private HttpAppender httpAppender;
	private OkHttpClient mockClient;
	private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

	@BeforeEach
	public void setUp() {
		mockClient = mock(OkHttpClient.class);

		httpAppender = new HttpAppender();
		httpAppender.setUrl("http://localhost:8080/log");
		httpAppender.setProjectName("TestProject");
		httpAppender.setProjectVersion("1.0");
		httpAppender.setLogVersion("1.0");
		httpAppender.setLogSource("TestSource");
		httpAppender.setLogType("TestType");
		httpAppender.setHost("localhost");
		httpAppender.setSecretKey("secret");
		httpAppender.setLogLevel("INFO");
		httpAppender.setPlatform("TestPlatform");

		httpAppender.setClient(mockClient); // set the mocked client
	}

	@Test
	void testAppend() throws IOException {
		// Given
		ILoggingEvent mockEvent = mock(LoggingEvent.class);
		when(mockEvent.getFormattedMessage()).thenReturn("Test log message");

		Call mockCall = mock(Call.class);
		Response mockResponse = new Response.Builder()
			.request(new Request.Builder().url("http://localhost:8080/log").build())
			.protocol(Protocol.HTTP_1_1)
			.code(200)
			.message("OK")
			.body(ResponseBody.create("", JSON))
			.build();
		when(mockCall.execute()).thenReturn(mockResponse);
		when(mockClient.newCall(any(Request.class))).thenReturn(mockCall);

		// When
		httpAppender.append(mockEvent);

		// Then
		verify(mockClient).newCall(any(Request.class));
	}

	@Test
	void testAppend_withIOException() throws IOException {
		// Given
		ILoggingEvent mockEvent = mock(LoggingEvent.class);
		when(mockEvent.getFormattedMessage()).thenReturn("Test log message");

		Call mockCall = mock(Call.class);
		when(mockCall.execute()).thenThrow(new IOException("Test IOException"));
		when(mockClient.newCall(any(Request.class))).thenReturn(mockCall);

		// When
		httpAppender.append(mockEvent);

		// Then
		verify(mockClient).newCall(any(Request.class));
	}
}

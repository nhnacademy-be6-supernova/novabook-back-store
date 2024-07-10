package store.novabook.store.common.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Setter;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;

@Setter
public class HttpAppender extends AppenderBase<ILoggingEvent> {

	private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
	private final OkHttpClient client = new OkHttpClient();
	private final ObjectMapper objectMapper = new ObjectMapper();

	private String url;
	private String projectName;
	private String projectVersion;
	private String logVersion;
	private String logSource;
	private String logType;
	private String host;
	private String secretKey;
	private String logLevel;
	private String platform;


	@Override
	protected void append(ILoggingEvent eventObject) {
		Response response = null;
		try {
			LogEvent logEvent = new LogEvent(
				projectName, projectVersion, logVersion, eventObject.getFormattedMessage(),
				logSource, logType, host, secretKey, logLevel, platform
			);
			String json = objectMapper.writeValueAsString(logEvent);
			RequestBody body = RequestBody.create(json, JSON);
			Request request = new Request.Builder()
				.url(url)
				.post(body)
				.build();
			response = client.newCall(request).execute();
		} catch (IOException e) {
			addError("Failed to send log event", e);
		} finally {
			if (response != null) {
				response.close();
			}
		}
	}

}

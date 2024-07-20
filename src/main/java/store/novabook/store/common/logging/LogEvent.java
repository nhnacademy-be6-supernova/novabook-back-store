package store.novabook.store.common.logging;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LogEvent {
	private String projectName;
	private String projectVersion;
	private String logVersion;
	private String body;
	private String logSource;
	private String logType;
	private String host;
	private String secretKey;
	private String logLevel;
	private String platform;
}

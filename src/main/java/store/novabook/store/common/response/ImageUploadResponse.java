package store.novabook.store.common.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ImageUploadResponse {

	@JsonProperty("successes")
	private List<UploadSuccessRecord> successes;

	@Data
	public static class UploadSuccessRecord {
		private boolean isFolder;
		private String id;
		private String url;
		private String name;
		private String path;
		private long bytes;
		private String createdBy;
		private String updatedAt;
		private String operationId;
		private ImageProperty imageProperty;
		private List<QueueRecord> queues;

		@Data
		public static class ImageProperty {
			private int width;
			private int height;
			private String createdAt;
			private Coordinate coordinate;

			@Data
			public static class Coordinate {
				private Double lat;
				private Double lng;
			}
		}

		@Data
		public static class QueueRecord {
			private String queueId;
			private String queueType;
			private String status;
			private int tryCount;
			private String queuedAt;
			private String operationId;
			private String url;
			private String name;
			private String path;
		}
	}
}

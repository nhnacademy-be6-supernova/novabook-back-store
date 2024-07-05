package store.novabook.store.common.util;

import java.util.List;

import lombok.Builder;

@Builder
public record ParamsDTO(
	String basepath,
	boolean overwrite,
	boolean autorename,
	List<String> operationIds,
	String callbackUrl
) {
}

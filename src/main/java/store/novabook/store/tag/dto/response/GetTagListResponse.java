package store.novabook.store.tag.dto.response;

import java.util.List;

import lombok.Builder;

@Builder
public record GetTagListResponse(
	List<GetTagResponse> getTagResponseList) {
}

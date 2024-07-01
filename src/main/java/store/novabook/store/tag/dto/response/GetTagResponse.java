package store.novabook.store.tag.dto.response;

import lombok.Builder;
import store.novabook.store.tag.entity.Tag;
@Builder
public record GetTagResponse(Long id, String name) {
	public static GetTagResponse fromEntity(Tag tag) {
		return GetTagResponse.builder().name(tag.getName()).id(tag.getId()).build();
	}

}

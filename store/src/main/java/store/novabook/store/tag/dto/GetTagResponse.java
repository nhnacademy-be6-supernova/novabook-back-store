package store.novabook.store.tag.dto;

import lombok.Builder;
import store.novabook.store.tag.entity.Tag;
@Builder
public record GetTagResponse(String name) {
	public static GetTagResponse fromEntity(Tag tag) {
		return GetTagResponse.builder().name(tag.getName()).build();
	}

}

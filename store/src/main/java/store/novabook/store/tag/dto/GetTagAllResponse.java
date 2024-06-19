package store.novabook.store.tag.dto;

import lombok.Builder;
import store.novabook.store.tag.entity.Tag;
@Builder
public record GetTagAllResponse(String name) {
	public static GetTagAllResponse fromEntity(Tag tag) {
		return GetTagAllResponse.builder().name(tag.getName()).build();
	}

}

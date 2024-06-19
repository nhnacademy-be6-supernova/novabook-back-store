package store.novabook.store.book.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import store.novabook.store.book.entity.Tag;
@Builder
public record GetTagAllResponse(String name) {
	public static GetTagAllResponse fromEntity(Tag tag) {
		return GetTagAllResponse.builder().name(tag.getName()).build();
	}

}

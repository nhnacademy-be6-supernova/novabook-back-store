package store.novabook.store.category.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record GetCategoryListResponse(List<GetCategoryResponse> categories) {
}

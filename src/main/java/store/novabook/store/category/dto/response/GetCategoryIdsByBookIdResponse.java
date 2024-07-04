package store.novabook.store.category.dto.response;

import java.util.ArrayList;
import java.util.List;

import store.novabook.store.category.entity.BookCategory;

public record GetCategoryIdsByBookIdResponse(List<Long> categoryIds){
}

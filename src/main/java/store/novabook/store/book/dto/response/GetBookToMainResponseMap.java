package store.novabook.store.book.dto.response;

import java.util.List;
import java.util.Map;

public record GetBookToMainResponseMap(
	Map<String, List<GetBookToMainResponse>> mainBookData
) {
}

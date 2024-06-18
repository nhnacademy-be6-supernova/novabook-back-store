package store.novabook.store.book.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import store.novabook.store.book.entity.BookStatus;

public record CreateBookRequest(BookStatus bookStatus,
								String isbn,
								String title,
								String subTitle,
								String engTitle,
								String index,
								String explanation,
								String translator,
								String publisher,
								LocalDateTime publicationDate,
								int inventory,
								Long price,
								boolean isPackaged,
								String image) {

}

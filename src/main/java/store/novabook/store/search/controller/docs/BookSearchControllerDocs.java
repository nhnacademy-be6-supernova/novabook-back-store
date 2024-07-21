package store.novabook.store.search.controller.docs;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import store.novabook.store.search.dto.GetBookSearchResponse;

/**
 * 책 검색 API 문서화 인터페이스입니다.
 * 이 인터페이스는 제목, 저자, 출판사, 카테고리별로 책을 검색하는 기능을 문서화합니다.
 */
@Tag(name = "BookSearch API", description = "책 검색 API입니다.")
public interface BookSearchControllerDocs {

	/**
	 * 제목을 기준으로 책을 검색합니다.
	 *
	 * @param title 검색할 제목
	 * @param pageable 페이지 정보
	 * @return 제목으로 검색된 책 목록
	 */
	@Operation(summary = "제목으로 책 검색", description = "제목을 기준으로 책을 검색합니다.")
	@Parameter(name = "title", description = "검색할 제목", required = true)
	@Parameter(name = "pageable", description = "페이지 정보", required = true)
	Page<GetBookSearchResponse> searchByKeyword(@RequestParam String title, Pageable pageable);

	/**
	 * 저자를 기준으로 책을 검색합니다.
	 *
	 * @param author 검색할 저자
	 * @param pageable 페이지 정보
	 * @return 저자로 검색된 책 목록
	 */
	@Operation(summary = "저자별 책 검색", description = "저자를 기준으로 책을 검색합니다.")
	@Parameter(name = "author", description = "검색할 저자", required = true)
	@Parameter(name = "pageable", description = "페이지 정보", required = true)
	Page<GetBookSearchResponse> searchByAuthor(@RequestParam String author, Pageable pageable);

	/**
	 * 출판사를 기준으로 책을 검색합니다.
	 *
	 * @param publish 검색할 출판사
	 * @param pageable 페이지 정보
	 * @return 출판사로 검색된 책 목록
	 */
	@Operation(summary = "출판사별 책 검색", description = "출판사를 기준으로 책을 검색합니다.")
	@Parameter(name = "publish", description = "검색할 출판사", required = true)
	@Parameter(name = "pageable", description = "페이지 정보", required = true)
	Page<GetBookSearchResponse> searchByPublish(@RequestParam String publish, Pageable pageable);

	/**
	 * 카테고리를 기준으로 책을 검색합니다.
	 *
	 * @param category 검색할 카테고리
	 * @param pageable 페이지 정보
	 * @return 카테고리로 검색된 책 목록
	 */
	@Operation(summary = "카테고리별 책 검색", description = "카테고리를 기준으로 책을 검색합니다.")
	@Parameter(name = "category", description = "검색할 카테고리", required = true)
	@Parameter(name = "pageable", description = "페이지 정보", required = true)
	Page<GetBookSearchResponse> searchByCategory(@RequestParam String category, Pageable pageable);
}

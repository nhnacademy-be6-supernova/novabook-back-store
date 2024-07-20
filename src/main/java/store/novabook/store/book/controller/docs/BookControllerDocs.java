package store.novabook.store.book.controller.docs;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import store.novabook.store.book.dto.request.CreateBookRequest;
import store.novabook.store.book.dto.request.UpdateBookRequest;
import store.novabook.store.book.dto.response.CreateBookResponse;
import store.novabook.store.book.dto.response.GetBookAllResponse;
import store.novabook.store.book.dto.response.GetBookResponse;
import store.novabook.store.book.dto.response.GetBookToMainResponseMap;

/**
 * 도서 관련 API 요청을 처리하는 컨트롤러 문서화 인터페이스.
 */
@Tag(name = "Book API")
public interface BookControllerDocs {
	/**
	 * 특정 도서 정보를 조회합니다.
	 *
	 * @param id 도서 ID
	 * @return ResponseEntity<GetBookResponse> 도서 정보와 HTTP 상태 코드를 포함한 응답
	 */
	@Operation(summary = "도서 정보 조회", description = "특정 도서 정보를 조회합니다.")
	ResponseEntity<GetBookResponse> getBook(@Parameter(description = "도서 ID", required = true) @PathVariable Long id);

	/**
	 * 메인 페이지에 표시할 도서 정보를 조회합니다.
	 *
	 * @return ResponseEntity<GetBookToMainResponseMap> 메인 페이지 도서 정보와 HTTP 상태 코드를 포함한 응답
	 */
	@Operation(summary = "메인 페이지 도서 조회", description = "메인 페이지에 표시할 도서 정보를 조회합니다.")
	ResponseEntity<GetBookToMainResponseMap> getBookToMainPage();

	/**
	 * 모든 도서 정보를 페이지네이션하여 조회합니다.
	 *
	 * @param pageable 페이지네이션 정보
	 * @return ResponseEntity<Page < GetBookAllResponse>> 도서 목록과 HTTP 상태 코드를 포함한 응답
	 */
	@Operation(summary = "모든 도서 조회", description = "모든 도서 정보를 페이지네이션하여 조회합니다.")
	ResponseEntity<Page<GetBookAllResponse>> getBookAll(
		@Parameter(description = "페이지네이션 정보", required = true) Pageable pageable);

	/**
	 * 새로운 도서를 생성합니다.
	 *
	 * @param createBookRequest 도서 생성 요청 정보
	 * @return ResponseEntity<CreateBookResponse> 생성된 도서 정보와 HTTP 상태 코드를 포함한 응답
	 */
	@Operation(summary = "도서 생성", description = "새로운 도서를 생성합니다.")
	ResponseEntity<CreateBookResponse> createBook(
		@Parameter(description = "도서 생성 요청 정보", required = true) @Valid @RequestBody CreateBookRequest createBookRequest);

	/**
	 * 기존 도서 정보를 수정합니다.
	 *
	 * @param updateBookRequest 도서 수정 요청 정보
	 * @return ResponseEntity<Void> 응답 상태 코드
	 */
	@Operation(summary = "도서 수정", description = "기존 도서 정보를 수정합니다.")
	ResponseEntity<Void> updateBook(
		@Parameter(description = "도서 수정 요청 정보", required = true) @Valid @RequestBody UpdateBookRequest updateBookRequest);

	/**
	 * 특정 도서를 삭제합니다.
	 *
	 * @param id 도서 ID
	 * @return ResponseEntity<Void> 응답 상태 코드
	 */
	@Operation(summary = "도서 삭제", description = "특정 도서를 삭제합니다.")
	ResponseEntity<Void> deleteBook(@Parameter(description = "도서 ID", required = true) @PathVariable Long id);
}

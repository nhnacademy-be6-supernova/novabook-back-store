package store.novabook.store.book.controller.docs;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import store.novabook.store.book.dto.request.CreateBookRequest;
import store.novabook.store.book.dto.request.UpdateBookRequest;
import store.novabook.store.book.dto.response.CreateBookResponse;
import store.novabook.store.book.dto.response.GetBookAllResponse;
import store.novabook.store.book.dto.response.GetBookResponse;

@Tag(name = "Book API")
public interface BookControllerDocs {

	@Operation(summary = "도서 정보 조회", description = "ID를 이용해 특정 도서의 정보를 조회합니다.")
	@ApiResponse(responseCode = "200", description = "도서 정보 조회에 성공하였습니다.", content = {
		@Content(mediaType = "application/json", schema = @Schema(implementation = GetBookResponse.class))})
	ResponseEntity<GetBookResponse> getBook(@PathVariable Long id);

	@Operation(summary = "도서 페이지 목록 조회", description = "페이지네이션을 사용해 전체 도서 목록을 조회합니다.")
	@ApiResponse(responseCode = "200", description = "도서 목록 조회에 성공하였습니다.", content = {
		@Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))})
	ResponseEntity<Page<GetBookAllResponse>> getBookAll(Pageable pageable);

	@Operation(summary = "도서 생성", description = "새로운 도서를 생성합니다.")
	@ApiResponse(responseCode = "201", description = "도서 생성에 성공하였습니다.", content = {
		@Content(mediaType = "application/json", schema = @Schema(implementation = CreateBookResponse.class))})
	ResponseEntity<CreateBookResponse> createBook(@Valid @RequestBody CreateBookRequest createBookRequest);

	@Operation(summary = "도서 업데이트", description = "기존 도서의 정보를 업데이트합니다.")
	@ApiResponse(responseCode = "200", description = "도서 업데이트에 성공하였습니다.")
	ResponseEntity<Void> updateBook(@Valid @RequestBody UpdateBookRequest updateBookRequest);

	@Operation(summary = "도서 삭제", description = "ID를 이용해 특정 도서를 삭제합니다.")
	@ApiResponse(responseCode = "204", description = "도서 삭제에 성공하였습니다.")
	ResponseEntity<Void> deleteBook(@PathVariable Long id);
}

package store.novabook.store.cart.controller.docs;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import store.novabook.store.cart.dto.CreateCartBookRequest;
import store.novabook.store.cart.dto.DeleteCartBookRequest;
import store.novabook.store.cart.dto.GetCartBookResponse;

@Tag(name = "CartBook API", description = "장바구니 - 도서 관계 테이블 API")
public interface CartBookControllerDocs {
	@Operation(summary = "<장바구니도서> 페이지 조회", description = "<장바구니도서>들을 조회합니다.")
	@Parameter(name = "cartId", description = "장바구니 ID", required = true)
	ResponseEntity<Page<GetCartBookResponse>> getAllCartBook(@PathVariable Long cartId, Pageable pageable);

	@Operation(summary = "<장바구니도서> 생성", description = "<장바구니도서>를 추가합니다.")
	@Parameter(name = "createCartBookRequest", description = "장바구니 도서 추가 정보", required = true)
	ResponseEntity<Void> createCartBook(@Valid @RequestBody CreateCartBookRequest createCartBookRequest);

	@Operation(summary = "<장바구니도서> 삭제", description = "<장바구니도서>를 삭제합니다.")
	@Parameter(name = "deleteCartBookRequest", description = "장바구니 도서 삭제 정보", required = true)
	ResponseEntity<Void> deleteCartBook(@Valid @RequestBody DeleteCartBookRequest deleteCartBookRequest);
}

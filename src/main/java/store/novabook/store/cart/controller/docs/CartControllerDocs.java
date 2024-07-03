package store.novabook.store.cart.controller.docs;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import store.novabook.store.cart.dto.request.CreateCartRequest;
import store.novabook.store.cart.dto.response.GetCartResponse;

@Tag(name = "Cart API")
public interface CartControllerDocs {
	@Operation(summary = "장바구니 조회", description = "장바구니를 조회합니다.")
	@Parameter(name = "memberId", description = "회원 ID", required = true)
	ResponseEntity<GetCartResponse> getCartByMemberID();
	@Operation(summary = "장바구니 생성", description = "장바구니를 생성합니다.")
	@Parameter(name = "createCartRequest", description = "장바구니에 존재하는 MemberID를 포합니다.", required = true)
	ResponseEntity<Void> createCart(@Valid @RequestBody CreateCartRequest createCartRequest);

}

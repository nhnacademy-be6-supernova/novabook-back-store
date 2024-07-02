package store.novabook.store.cart.dto.request;

public record DeleteCartBookRequest(
	Long cartBookId,
	Long bookId
) {
	public static DeleteCartBookRequest from(Long cartBookId, Long bookId) {
		return new DeleteCartBookRequest(cartBookId, bookId);
	}
}

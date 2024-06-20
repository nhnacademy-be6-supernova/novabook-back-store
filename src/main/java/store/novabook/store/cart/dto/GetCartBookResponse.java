package store.novabook.store.cart.dto;

public record GetCartBookResponse(
	int cartBookQuantity,
	String bookTitle,
	Long bookPrice,
	Long bookDiscountPrice,
	String thumbnail) {
}

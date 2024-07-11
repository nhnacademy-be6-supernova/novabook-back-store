package store.novabook.store.orders.dto.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import store.novabook.store.orders.entity.OrdersBook;

@Builder
public record GetOrderDetailResponse(
	Long ordersId,
	Long ordersStatusId,
	String ordersStatusName,
	List<String> bookTitle,
	Long price,
	Integer quantity,
	Long deliveryFee,
	Long wrappingFee,
	String receiverName,
	String receiverNumber,
	String receiverAddress,
	LocalDateTime expectedDeliveryDate,
	Long totalPrice
	// Long couponDiscountPrice,
	// Long finalPrice,
	// Long pointsSave
) {
	public static GetOrderDetailResponse of(List<OrdersBook> ordersBook) {
		List<String> bookTitle = new ArrayList<>();
		int quantity = 0;
		long totalPrice = 0L;
		// Long couponDiscountPrice = 0L;
		// Long finalPrice = 0L;
		// Long pointsSave = 0L;
		for (OrdersBook book : ordersBook) {
			bookTitle.add(book.getBook().getTitle());
			quantity += book.getQuantity();
			totalPrice += book.getPrice();
			// couponDiscountPrice += book.getOrders().getCouponDiscounprice();
		}

		return GetOrderDetailResponse.builder()
			.ordersId(ordersBook.getFirst().getOrders().getId())
			.ordersStatusId(ordersBook.getFirst().getOrders().getOrdersStatus().getId())
			.ordersStatusName(ordersBook.getFirst().getOrders().getOrdersStatus().getName())
			.bookTitle(bookTitle)
			.quantity(quantity)
			.deliveryFee(ordersBook.getFirst().getOrders().getDeliveryFee().getId())
			.wrappingFee(ordersBook.getFirst().getOrders().getWrappingPaper().getId())
			.receiverName(ordersBook.getFirst().getOrders().getReceiverName())
			.receiverNumber(ordersBook.getFirst().getOrders().getReceiverNumber())
			.receiverAddress(ordersBook.getLast().getOrders().getDeliveryAddress())
			.expectedDeliveryDate(ordersBook.getFirst().getOrders().getDeliveryDate())
			.totalPrice(totalPrice)
			// .couponDiscountPrice(ordersBook.getOrders().getCouponDiscountPrice)
			// .finalPrice(ordersBook.getPrice())
			// .pointsSave(ordersBook.getOrders().getPointSave())
			.build();
	}
}

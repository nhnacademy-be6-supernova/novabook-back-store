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
	Integer quantity,
	Long deliveryFee,
	Long wrappingFee,
	String receiverName,
	String receiverNumber,
	String receiverAddress,
	LocalDateTime expectedDeliveryDate,
	Long totalPrice,
	Long couponDiscountAmount,
	Long finalAmount,
	Long pointSaveAmount
) {
	public static GetOrderDetailResponse of(List<OrdersBook> ordersBook) {
		List<String> bookTitle = new ArrayList<>();
		int quantity = 0;

		for (OrdersBook book : ordersBook) {
			bookTitle.add(book.getBook().getTitle());
			quantity += book.getQuantity();
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
			.totalPrice(ordersBook.getFirst().getOrders().getBookPurchaseAmount())
			.couponDiscountAmount(ordersBook.getFirst().getOrders().getCouponDiscountAmount())
			.finalAmount(ordersBook.getFirst().getOrders().getTotalAmount())
			.pointSaveAmount(ordersBook.getFirst().getOrders().getPointSaveAmount())
			.build();
	}
}

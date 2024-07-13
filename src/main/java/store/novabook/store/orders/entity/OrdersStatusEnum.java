package store.novabook.store.orders.entity;

public enum OrdersStatusEnum {
	PENDING(1, "대기"),
	SHIPPING(2, "배송중"),
	COMPLETED(3, "완료"),
	RETURNED(4, "반품"),
	CANCELED(5, "주문 취소");

	private final int code;
	private final String description;

	OrdersStatusEnum(int code, String description) {
		this.code = code;
		this.description = description;
	}

	public int getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		return this.description;
	}
}
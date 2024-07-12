package store.novabook.store.orders.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import store.novabook.store.member.entity.Member;
import store.novabook.store.orders.dto.request.CreateOrdersRequest;
import store.novabook.store.payment.entity.Payment;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Orders {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "delivery_fee_id")
	private DeliveryFee deliveryFee;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "wrapping_paper_id")
	private WrappingPaper wrappingPaper;

	@NotNull
	private String uuid;

	@NotNull
	@ManyToOne
	@Setter
	@JoinColumn(name = "orders_status_id")
	private OrdersStatus ordersStatus;

	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;

	@NotNull
	private LocalDateTime ordersDate;

	@NotNull
	private Long totalAmount;

	@NotNull
	private LocalDateTime deliveryDate;

	@NotNull
	private long bookPurchaseAmount;

	@NotNull
	private String deliveryAddress;

	@NotNull
	private String receiverName;

	@OneToOne
	private Payment payment;

	@NotNull
	private String senderName;

	@NotNull
	private String senderNumber;

	@NotNull
	private String receiverNumber;

	private Long pointSaveAmount;

	private Long couponDiscountAmount;

	@NotNull
	@CreatedDate
	private LocalDateTime createdAt;

	@LastModifiedDate
	private LocalDateTime updatedAt;

	@Builder
	public Orders(Member member,
		DeliveryFee deliveryFee,
		WrappingPaper wrappingPaper,
		OrdersStatus ordersStatus,
		Payment payment,
		CreateOrdersRequest request) {

		this.member = member;
		this.deliveryFee = deliveryFee;
		this.wrappingPaper = wrappingPaper;
		this.ordersStatus = ordersStatus;
		this.payment = payment;
		this.ordersDate = LocalDateTime.now();
		this.totalAmount = request.totalAmount();
		this.deliveryDate = request.deliveryDate();
		this.bookPurchaseAmount = request.bookPurchaseAmount();
		this.deliveryAddress = request.deliveryAddress();
		this.uuid = request.uuid();
		this.senderName = request.senderName();
		this.senderNumber = request.senderNumber();
		this.receiverName = request.receiverName();
		this.receiverNumber = request.receiverNumber();
		this.pointSaveAmount = request.pointSaveAmount();
		this.couponDiscountAmount = request.couponDiscountAmount();
	}

	public void updateStatus(
		OrdersStatus ordersStatus) {
		this.ordersStatus = ordersStatus;
	}
}

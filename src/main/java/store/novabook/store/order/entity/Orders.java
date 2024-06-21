package store.novabook.store.order.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.novabook.store.order.dto.CreateOrdersRequest;
import store.novabook.store.order.dto.UpdateOrdersRequest;
import store.novabook.store.user.member.entity.Users;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Orders {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "users_id")
	private Users users;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "delivery_fee_id")
	private DeliveryFee deliveryFee;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "wrapping_paper_id")
	private WrappingPaper wrappingPaper;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "orders_status_id")
	private OrdersStatus ordersStatus;

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
	private String receieverName;

	@NotNull
	private String receieverNumber;

	@NotNull
	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	public Orders(Users users,
		DeliveryFee deliveryFee,
		WrappingPaper wrappingPaper,
		OrdersStatus ordersStatus,
		ReturnPolicy returnPolicy,
		CreateOrdersRequest request){
		this.users = users;
		this.deliveryFee = deliveryFee;
		this.wrappingPaper = wrappingPaper;
		this.ordersStatus = ordersStatus;
		this.returnPolicy = returnPolicy;
		this.ordersDate = LocalDateTime.now();
		this.totalAmount = request.totalAmount();
		this.deliveryDate = request.deliveryDate();
		this.bookPurchaseAmount = request.bookPurchaseAmount();
		this.deliveryAddress = request.deliveryAddress();
		this.receieverName = request.recieverName();
		this.receieverNumber = request.recieverNumber();
		this.createdAt = LocalDateTime.now();
	}
	public void update(Users users,
		DeliveryFee deliveryFee,
		WrappingPaper wrappingPaper,
		OrdersStatus ordersStatus,
		ReturnPolicy returnPolicy,
		UpdateOrdersRequest request){
		this.users = users;
		this.deliveryFee = deliveryFee;
		this.wrappingPaper = wrappingPaper;
		this.ordersStatus = ordersStatus;
		this.returnPolicy = returnPolicy;
		this.ordersDate = LocalDateTime.now();
		this.totalAmount = request.totalAmount();
		this.deliveryDate = request.deliveryDate();
		this.bookPurchaseAmount = request.bookPurchaseAmount();
		this.deliveryAddress = request.deliveryAddress();
		this.receieverName = request.recieverName();
		this.receieverNumber = request.recieverNumber();
		this.updatedAt = LocalDateTime.now();
	}
}

package store.novabook.store.order.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.novabook.store.order.dto.CreateOrdersStatusRequest;
import store.novabook.store.order.dto.UpdateOrdersStatusRequest;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class OrdersStatus {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private String name;

	@NotNull
	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	public OrdersStatus(CreateOrdersStatusRequest request){
		this.name = request.name();
		this.createdAt = LocalDateTime.now();
	}

	public void update(UpdateOrdersStatusRequest request){
		this.updatedAt = LocalDateTime.now();
		this.name = request.name();
	}
}

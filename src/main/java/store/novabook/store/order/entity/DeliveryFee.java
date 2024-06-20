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
import store.novabook.store.order.dto.CreateDeliveryFeeRequest;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class DeliveryFee {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private long fee;

	@NotNull
	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	public DeliveryFee(CreateDeliveryFeeRequest createDeliveryFeeRequest) {
		this.fee = createDeliveryFeeRequest.fee();
		createdAt = LocalDateTime.now();
	}

}

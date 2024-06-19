package store.novabook.store.payment.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.novabook.store.payment.dto.CreatePaymentRequest;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Payment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long ordersId;

	@NotNull
	private String provider;

	private String paymentKey;

	@NotNull
	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	public Payment(CreatePaymentRequest request) {
		this.ordersId = request.ordersId();
		this.provider = request.provider();
		this.paymentKey = request.paymentKey();
		this.createdAt = LocalDateTime.now();
		this.updatedAt = null;
	}
}

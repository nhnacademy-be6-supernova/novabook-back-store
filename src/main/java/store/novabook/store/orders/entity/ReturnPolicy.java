package store.novabook.store.orders.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.novabook.store.orders.dto.CreateReturnPolicyRequest;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ReturnPolicy {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private String content;

	@NotNull
	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	public ReturnPolicy(CreateReturnPolicyRequest request) {
		this.content = request.content();
		this.createdAt = LocalDateTime.now();
	}
}

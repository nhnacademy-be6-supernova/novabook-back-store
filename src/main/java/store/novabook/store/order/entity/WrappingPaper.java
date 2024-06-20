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
import store.novabook.store.order.dto.CreateWrappingPaperRequest;
import store.novabook.store.order.dto.UpdateWrappingPaperRequest;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class WrappingPaper {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private String name;

	@NotNull
	private long price;

	@NotNull
	private String status;

	@NotNull
	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	public WrappingPaper(CreateWrappingPaperRequest request) {
		this.name = request.name();
		this.price = request.price();
		this.status = request.status();
		this.createdAt = LocalDateTime.now();
	}

	public void updated(UpdateWrappingPaperRequest request) {
		this.updatedAt = LocalDateTime.now();
		this.price = request.price();
		this.status = request.status();
	}
}

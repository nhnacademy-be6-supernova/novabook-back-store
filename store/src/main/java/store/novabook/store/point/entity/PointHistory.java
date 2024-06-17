package store.novabook.store.point.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.novabook.store.order.entity.Orders;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class PointHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "orders_id")
	private Orders orders;

	@NotNull
	@OneToOne
	@JoinColumn(name = "point_policy_id")
	private PointPolicy pointPolicy;

	@NotNull
	private String pointContent;

	@NotNull
	private long pointAmount;

	@NotNull
	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;
}

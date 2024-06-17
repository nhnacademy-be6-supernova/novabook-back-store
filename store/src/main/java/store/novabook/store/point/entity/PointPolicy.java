package store.novabook.store.point.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PointPolicy {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(mappedBy = "pointPolicy")
	private PointHistory pointHistory;

	@NotNull
	@Column(precision = 10, scale = 2)
	private BigDecimal reviewPointRate;

	@NotNull
	@Column(precision = 10, scale = 2)
	private BigDecimal basicPoint;

	@NotNull
	@Column(precision = 10, scale = 2)
	private BigDecimal registerPoint;

	@NotNull
	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

}

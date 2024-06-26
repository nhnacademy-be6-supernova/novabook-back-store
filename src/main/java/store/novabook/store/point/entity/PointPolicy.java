package store.novabook.store.point.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@EntityListeners(AuditingEntityListener.class)
public class PointPolicy {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private long reviewPointRate;

	@NotNull
	private long basicPoint;

	@NotNull
	private long registerPoint;

	@NotNull
	@CreatedDate
	private LocalDateTime createdAt;

	@LastModifiedDate
	private LocalDateTime updatedAt;


	@Builder
	public PointPolicy(long reviewPointRate, long basicPoint, long registerPoint) {
		this.reviewPointRate = reviewPointRate;
		this.basicPoint = basicPoint;
		this.registerPoint = registerPoint;

	}

	public static PointPolicy of(long reviewPointRate, long basicPoint, long registerPoint) {
		return PointPolicy.builder().reviewPointRate(reviewPointRate).basicPoint(basicPoint).registerPoint(registerPoint).build();
	}

}

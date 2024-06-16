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
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PointHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	//TODO: Orders테이블 만들면 주석해제
	// @ManyToOne()
	// private Orders orders;

	@NotNull
	@OneToOne
	@JoinColumn(name = "point_policy_id")
	private PointPolicy pointPolicy;

	@NotNull
	private String pointContent;

	private Long pointAmount;

	@NotNull
	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;
}

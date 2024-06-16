package store.novabook.store.user.member.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class MemberGrade {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private String name;

	@NotNull
	@Column(precision =10, scale = 2)
	private BigDecimal minRange;

	@NotNull
	@Column(precision =10, scale = 2)
	private BigDecimal maxRange;

	@NotNull
	@Column(precision =10, scale = 2)
	private BigDecimal discountRate;

	@NotNull
	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

}

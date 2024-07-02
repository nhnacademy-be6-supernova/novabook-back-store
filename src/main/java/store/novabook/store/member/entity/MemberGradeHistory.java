package store.novabook.store.member.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.novabook.store.common.validator.ValidQuarter;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class MemberGradeHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@ValidQuarter
	private String quarter;

	@NotNull
	@CreatedDate
	private LocalDateTime createdAt;

	@LastModifiedDate
	private LocalDateTime updatedAt;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "member_grade_policy_id")
	private MemberGradePolicy memberGradePolicy;

	@Builder
	public MemberGradeHistory(Member member, MemberGradePolicy memberGradePolicy, LocalDateTime quarter) {
		this.member = member;
		this.memberGradePolicy = memberGradePolicy;
		this.quarter = getPreviousQuarterFromDateTime(quarter);
	}

	private String getPreviousQuarterFromDateTime(LocalDateTime dateTime) {
		int year = dateTime.getYear();
		int month = dateTime.getMonthValue();

		int previousQuarter = ((month - 1) / 3);
		if (previousQuarter == 0) {
			previousQuarter = 4;
			year -= 1;
		}

		return String.format("%dQ%d", year, previousQuarter);
	}

}

package store.novabook.store.user.member.entity;

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
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private String loginId;

	@NotNull
	private String loginPassword;

	@NotNull
	private String name;

	@NotNull
	private String number;

	@NotNull
	private String email;

	@NotNull
	private LocalDateTime birth;

	@NotNull
	private Long point;

	@NotNull
	private LocalDateTime latestLoginAt;

	@NotNull
	private Long totalAmount;

	@NotNull
	private int authentication;

	@NotNull
	@CreatedDate
	private LocalDateTime createdAt;

	@NotNull
	@LastModifiedDate
	private LocalDateTime updatedAt;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "member_grade_id")
	private MemberGrade memberGrade;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "member_status_id")
	private MemberStatus memberStatus;

	@NotNull
	@OneToOne
	@JoinColumn(name = "users_id")
	private Users users;

}

package store.novabook.store.user.member.entity;

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

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
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
	private LocalDateTime createdAt;

	@NotNull
	private LocalDateTime updatedAt;


	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "member_grade_id")
	private MemberGrade memberGrade;

	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "member_status_id")
	private MemberStatus memberStatus;

	@NotNull
	@OneToOne(optional = false)
	@JoinColumn(name = "user_id")
	private User user;


}

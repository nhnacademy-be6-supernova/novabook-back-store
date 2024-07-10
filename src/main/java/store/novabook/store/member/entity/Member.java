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
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.novabook.store.member.dto.request.CreateMemberRequest;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
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
	private LocalDateTime latestLoginAt;

	@NotNull
	private int authentication;

	@NotNull
	@CreatedDate
	private LocalDateTime createdAt;

	@LastModifiedDate
	private LocalDateTime updatedAt;

	private String paycoId;

	private String role;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "member_status_id")
	private MemberStatus memberStatus;

	@Builder
	public Member(String loginId,
		String loginPassword,
		String name,
		String number,
		String email,
		LocalDateTime birth,
		LocalDateTime latestLoginAt,
		int authentication,
		MemberStatus memberStatus,
		String role) {
		this.loginId = loginId;
		this.loginPassword = loginPassword;
		this.name = name;
		this.number = number;
		this.email = email;
		this.birth = birth;
		this.latestLoginAt = latestLoginAt;
		this.authentication = authentication;
		this.memberStatus = memberStatus;
		this.role = role;
	}

	public void update(String loginPassword, String name, String number, String email) {
		this.loginPassword = loginPassword;
		this.name = name;
		this.number = number;
		this.email = email;
	}

	public void updateMemberStatus(MemberStatus memberStatus) {
		this.memberStatus = memberStatus;
	}

	public static Member of(CreateMemberRequest createMemberRequest, MemberStatus memberStatus, LocalDateTime birth,
		String encodedPassword, String role) {
		return Member.builder()
			.memberStatus(memberStatus)
			.loginId(createMemberRequest.loginId())
			.loginPassword(createMemberRequest.loginPassword())
			.loginPassword(encodedPassword)
			.name(createMemberRequest.name())
			.number(createMemberRequest.number())
			.email(createMemberRequest.getEmailFull())
			.birth(birth)
			.latestLoginAt(LocalDateTime.now())
			.role(role)
			.build();
	}

	public void updateName(String name) {
		this.name = name;
	}

	public void updateNumber(String number) {
		this.number = number;
	}

	public void updateLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}
}

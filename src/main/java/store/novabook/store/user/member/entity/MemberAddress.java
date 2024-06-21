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
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.novabook.store.user.member.dto.CreateMemberAddressRequest;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class MemberAddress {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private String nickname;

	@NotNull
	private String memberAddressDetail;

	@NotNull
	@CreatedDate
	private LocalDateTime createdAt;

	@LastModifiedDate
	private LocalDateTime updatedAt;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "street_address_id")
	private StreetAddress streetAddress;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;

	public void update(StreetAddress streetAddress, String nickname, String memberAddressDetail) {
		this.streetAddress = streetAddress;
		this.nickname = nickname;
		this.memberAddressDetail = memberAddressDetail;
		this.updatedAt = LocalDateTime.now();
	}

	public static MemberAddress of(CreateMemberAddressRequest createMemberAddressRequest, Member member, StreetAddress streetAddress) {
		return MemberAddress.builder()
			.streetAddress(streetAddress)
			.member(member)
			.nickname(createMemberAddressRequest.nickname())
			.memberAddressDetail(createMemberAddressRequest.memberAddressDetail())
			.build();
	}

}


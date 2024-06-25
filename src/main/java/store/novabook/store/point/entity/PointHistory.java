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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.novabook.store.orders.entity.Orders;
import store.novabook.store.user.member.entity.Member;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@EntityListeners(AuditingEntityListener.class)
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

	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;

	@NotNull
	private String pointContent;

	@NotNull
	private long pointAmount;

	@NotNull
	@CreatedDate
	private LocalDateTime createdAt;

	@LastModifiedDate
	private LocalDateTime updatedAt;

	@Builder
	public PointHistory(Orders orders, PointPolicy pointPolicy, Member member, String pointContent, long pointAmount){
		this.orders = orders;
		this.pointPolicy = pointPolicy;
		this.member = member;
		this.pointContent = pointContent;
		this.pointAmount = pointAmount;
	}

	public static PointHistory of(PointPolicy pointPolicy, Orders orders, Member member, String pointContent, long pointAmount) {
		return PointHistory.builder()
			.orders(orders)
			.pointPolicy(pointPolicy)
			.member(member)
			.pointContent(pointContent)
			.pointAmount(pointAmount).build();
	}
}

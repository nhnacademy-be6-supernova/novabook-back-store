package store.novabook.store.cart.entity;

import static jakarta.persistence.GenerationType.*;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.novabook.store.user.member.entity.Users;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Cart {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "users_id")
	private Users users;

	@NotNull
	private Boolean isExposed;

	@NotNull
	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

}

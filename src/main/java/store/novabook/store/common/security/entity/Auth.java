package store.novabook.store.common.security.entity;

import java.io.Serializable;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
public class Auth implements Serializable {

	@NotNull
	String uuid;

	@NotNull
	long usersId;

	@NotNull
	String role;

	@Builder
	private Auth(String uuid, long usersId, String role) {
		this.uuid = uuid;
		this.usersId = usersId;
		this.role = role;
	}

	public static Auth of(String uuid, long usersId, String role) {
		return Auth.builder().uuid(uuid).usersId(usersId).role(role).build();
	}
}

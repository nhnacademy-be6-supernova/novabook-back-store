package store.novabook.store.common.security.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Users {
	private long id;

	private String username;
	private String password;

	private String role;
}

package store.novabook.store.common.security.aop;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;

import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import store.novabook.store.common.exception.UnauthorizedException;

@ExtendWith(MockitoExtension.class)
class RoleCheckAspectTest {

	@Mock
	private JoinPoint joinPoint;

	@Mock
	private CheckRole checkRole;

	private RoleCheckAspect roleCheckAspect;

	@BeforeEach
	void setUp() {
		roleCheckAspect = new RoleCheckAspect();
	}

	@Test
	void checkRole_withValidRole() {
		Authentication auth = new UsernamePasswordAuthenticationToken("user", "password",
			Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
		SecurityContextHolder.getContext().setAuthentication(auth);

		when(checkRole.value()).thenReturn(new String[] {"ROLE_USER"});

		assertDoesNotThrow(() -> roleCheckAspect.checkRole(joinPoint, checkRole));
	}

	@Test
	void checkRole_withoutAuthentication() {
		SecurityContextHolder.getContext().setAuthentication(null);
		assertThrows(UnauthorizedException.class, () -> roleCheckAspect.checkRole(joinPoint, checkRole));
	}

	@Test
	void checkRole_withAuthenticatedButNoRoles() {
		Authentication auth = new UsernamePasswordAuthenticationToken("user", "password", Collections.emptyList());
		SecurityContextHolder.getContext().setAuthentication(auth);

		when(checkRole.value()).thenReturn(new String[] {"ROLE_USER"});

		assertThrows(UnauthorizedException.class, () -> roleCheckAspect.checkRole(joinPoint, checkRole));
	}

}

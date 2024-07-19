package store.novabook.store.common.security.aop;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;

import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import store.novabook.store.common.exception.UnauthorizedException;

class RoleCheckAspectTest {

	@Mock
	private JoinPoint joinPoint;

	private RoleCheckAspect roleCheckAspect;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		roleCheckAspect = new RoleCheckAspect();
	}

	@Test
	void checkRole_withInvalidRole() {
		Authentication auth = new UsernamePasswordAuthenticationToken("user", "password",
			Collections.singletonList(new SimpleGrantedAuthority("ROLE_INVALID")));
		SecurityContextHolder.getContext().setAuthentication(auth);

		CheckRole checkRole = mock(CheckRole.class);
		when(checkRole.value()).thenReturn(new String[] {"ROLE_USER"});

		assertThrows(UnauthorizedException.class, () -> roleCheckAspect.checkRole(joinPoint, checkRole));
	}

	@Test
	void checkRole_withoutAuthentication() {
		SecurityContextHolder.getContext().setAuthentication(null);

		CheckRole checkRole = mock(CheckRole.class);
		when(checkRole.value()).thenReturn(new String[] {"ROLE_USER"});

		assertThrows(UnauthorizedException.class, () -> roleCheckAspect.checkRole(joinPoint, checkRole));
	}

	@Test
	void checkRole_withAuthenticatedButNoRoles() {
		Authentication auth = new UsernamePasswordAuthenticationToken("user", "password", Collections.emptyList());
		SecurityContextHolder.getContext().setAuthentication(auth);

		CheckRole checkRole = mock(CheckRole.class);
		when(checkRole.value()).thenReturn(new String[] {"ROLE_USER"});

		assertThrows(UnauthorizedException.class, () -> roleCheckAspect.checkRole(joinPoint, checkRole));
	}

}

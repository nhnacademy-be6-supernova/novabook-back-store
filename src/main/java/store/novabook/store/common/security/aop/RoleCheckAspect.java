package store.novabook.store.common.security.aop;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import store.novabook.store.common.exception.ErrorCode;
import store.novabook.store.common.exception.UnauthorizedException;

@Aspect
@Component
public class RoleCheckAspect {

	@Before("@annotation(checkRole)")
	public void checkRole(JoinPoint joinPoint, CheckRole checkRole) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
		}

		String[] requiredRoles = checkRole.value();
		boolean hasRole = authentication.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.anyMatch(role -> Arrays.asList(requiredRoles).contains(role));

		if (!hasRole) {
			throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
		}
	}
}

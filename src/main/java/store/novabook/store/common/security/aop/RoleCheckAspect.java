package store.novabook.store.common.security.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RoleCheckAspect {

	@Before("@annotation(checkRole)")
	public void checkRole(JoinPoint joinPoint, CheckRole checkRole) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			throw new Exception("Unauthorized");
		}

		String requiredRole = checkRole.value();
		boolean hasRole = authentication.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.anyMatch(role -> role.equals(requiredRole));

		if (!hasRole) {
			throw new Exception("Forbidden");
		}
	}
}
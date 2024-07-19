package store.novabook.store.common.config;

import java.io.IOException;
import java.util.Enumeration;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.util.annotation.NonNull;
import store.novabook.store.common.security.dto.CustomUserDetails;
import store.novabook.store.common.security.entity.AuthenticationMembers;

@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response,
		@NonNull FilterChain filterChain) throws ServletException, IOException {

		Enumeration<String> headerNames = request.getHeaderNames();
		headerNames.asIterator().forEachRemaining(headerName -> {
			log.error("headerName: {}", headerName);
			log.error("headerValue: {}", request.getHeader(headerName));
		});

		String membersId = request.getHeader("X-USER-ID");
		log.error("membersId: {}", membersId);
		String role = request.getHeader("X-USER-ROLE");
		log.error("role {}: ", role);

		if (membersId == null || role == null) {
			log.error("username or role is null");
			filterChain.doFilter(request, response);
			return;
		}

		AuthenticationMembers authenticationMembers = AuthenticationMembers.of(
			Long.parseLong(membersId),
			null,
			null,
			role
		);

		CustomUserDetails customUserDetails = new CustomUserDetails(authenticationMembers);
		Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null,
			customUserDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);

		filterChain.doFilter(request, response);
	}
}
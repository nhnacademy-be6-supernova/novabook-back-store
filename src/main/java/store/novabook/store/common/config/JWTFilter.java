package store.novabook.store.common.config;

import java.io.IOException;

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
import store.novabook.store.common.security.entity.Members;
import store.novabook.store.member.dto.request.GetMembersUUIDRequest;
import store.novabook.store.member.dto.response.GetMembersUUIDResponse;
import store.novabook.store.member.service.AuthMembersClient;

@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

	private final AuthMembersClient authMembersClient;

	@Override
	protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response,
		@NonNull FilterChain filterChain) throws ServletException, IOException {

		String username = request.getHeader("X-USER-ID");
		String role = request.getHeader("X-USER-ROLE");

		if (username == null || role == null) {
			log.error("username or role is null");
			filterChain.doFilter(request, response);
			return;
		}

		GetMembersUUIDRequest getMembersUUIDRequest = new GetMembersUUIDRequest(username);

		GetMembersUUIDResponse getMembersUUIDResponse = authMembersClient.getMembersId(getMembersUUIDRequest).getBody();

		Members members = new Members();
		members.setId(getMembersUUIDResponse.membersId());
		members.setUsername(Long.toString(getMembersUUIDResponse.membersId()));
		members.setPassword("temppassword");
		members.setRole(getMembersUUIDResponse.role());

		CustomUserDetails customUserDetails = new CustomUserDetails(members);

		Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null,
			customUserDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authToken);

		filterChain.doFilter(request, response);
	}
}
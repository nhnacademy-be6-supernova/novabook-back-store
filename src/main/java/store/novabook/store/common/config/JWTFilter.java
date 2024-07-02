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
import lombok.extern.slf4j.Slf4j;
import reactor.util.annotation.NonNull;
import store.novabook.store.common.security.dto.CustomUserDetails;
import store.novabook.store.common.security.entity.Users;
import store.novabook.store.member.MemberClient;
import store.novabook.store.member.dto.request.GetMembersUUIDRequest;
import store.novabook.store.member.dto.response.GetMembersUUIDResponse;

@Slf4j
public class JWTFilter extends OncePerRequestFilter {


	private final MemberClient memberClient;

	public JWTFilter(MemberClient memberClient) {

		this.memberClient = memberClient;
	}

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

		GetMembersUUIDResponse membersId = memberClient.getMembersId(getMembersUUIDRequest);

		//userEntity를 생성하여 값 set
		Users userEntity = new Users();
		userEntity.setId(Long.parseLong(membersId.usersId()));
		userEntity.setUsername(membersId.usersId());
		userEntity.setPassword("temppassword");
		userEntity.setRole(role);

		//UserDetails에 회원 정보 객체 담기
		CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);

		//스프링 시큐리티 인증 토큰 생성
		Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null,
			customUserDetails.getAuthorities());
		//세션에 사용자 등록
		SecurityContextHolder.getContext().setAuthentication(authToken);

		filterChain.doFilter(request, response);
	}
}
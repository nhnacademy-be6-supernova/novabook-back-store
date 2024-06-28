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
import store.novabook.store.common.security.dto.CustomUserDetails;
import store.novabook.store.common.security.entity.Users;
import store.novabook.store.user.member.MemberClient;
import store.novabook.store.user.member.dto.GetMembersUUIDRequest;
import store.novabook.store.user.member.dto.GetMembersUUIDResponse;

@Slf4j
public class JWTFilter extends OncePerRequestFilter {

	private final JWTUtil jwtUtil;

	private final MemberClient memberClient;

	public JWTFilter(JWTUtil jwtUtil, MemberClient memberClient) {

		this.jwtUtil = jwtUtil;
		this.memberClient = memberClient;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		String authorization = request.getHeader("Authorization");

		//Authorization 헤더 검증
		if (authorization == null) {

			log.error("token not found");
			filterChain.doFilter(request, response);

			//조건이 해당되면 메소드 종료 (필수)
			return;
		}

		//Bearer 부분 제거 후 순수 토큰만 획득
		String token = authorization.split(" ")[1];

		//토큰 소멸 시간 검증
		if (jwtUtil.isExpired(token)) {

			log.error("token expired");
			filterChain.doFilter(request, response);

			//조건이 해당되면 메소드 종료 (필수)
			return;
		}

		//토큰에서 username과 role 획득
		String username = jwtUtil.getUsername(token);
		String role = jwtUtil.getRole(token);

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
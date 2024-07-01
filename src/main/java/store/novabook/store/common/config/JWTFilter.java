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
import store.novabook.store.common.security.service.NewTokenClient;
import store.novabook.store.member.MemberClient;
import store.novabook.store.member.dto.request.GetMembersUUIDRequest;
import store.novabook.store.member.dto.response.GetMembersUUIDResponse;

@Slf4j
public class JWTFilter extends OncePerRequestFilter {

	private final JWTUtil jwtUtil;

	private final MemberClient memberClient;

	private final NewTokenClient newTokenClient;

	public JWTFilter(JWTUtil jwtUtil, MemberClient memberClient, NewTokenClient newTokenClient) {

		this.jwtUtil = jwtUtil;
		this.memberClient = memberClient;
		this.newTokenClient = newTokenClient;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response,
		@NonNull FilterChain filterChain) throws ServletException, IOException {

		String path = request.getRequestURI();

		if (path.startsWith("/api/v1/store/members/find")) {
			filterChain.doFilter(request, response);
			return;
		}

		String authorization = request.getHeader("Authorization");
		String refresh = request.getHeader("Refresh");

		//Authorization 헤더 검증
		if (authorization == null || refresh == null) {
			log.error("token not found");
			filterChain.doFilter(request, response);

			//조건이 해당되면 메소드 종료 (필수)
			return;
		}

		//Bearer 부분 제거 후 순수 토큰만 획득
		String token = authorization.split(" ")[1];

		//토큰 소멸 시간 검증
		if (Boolean.TRUE.equals(jwtUtil.isExpired(token))) {

			log.error("token expired");

			// GetNewTokenResponse newToken = newTokenClient.getNewToken(new GetNewTokenRequest(refresh));

			// Cookie expiredCookie = new Cookie("ZZZ", "QQQ");
			// expiredCookie.setPath("/"); // 경로 설정
			// expiredCookie.setHttpOnly(false); // HttpOnly 설정 여부
			// expiredCookie.setSecure(false); // Secure 설정 여부
			// response.addCookie(expiredCookie);

			// 응답 헤더 설정
			// response.setHeader("NewToken", newToken.accessToken());

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
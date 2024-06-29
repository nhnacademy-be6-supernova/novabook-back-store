package store.novabook.store.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import store.novabook.store.common.security.service.NewTokenClient;
import store.novabook.store.user.member.MemberClient;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final JWTUtil jwtUtil;
	private final MemberClient memberClient;
	private final NewTokenClient newTokenClient;

	public SecurityConfig(JWTUtil jwtUtil, MemberClient memberClient, NewTokenClient newTokenClient) {

		this.jwtUtil = jwtUtil;
		this.memberClient = memberClient;
		this.newTokenClient = newTokenClient;
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http
			.csrf(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests(authorize -> authorize
				.requestMatchers("/**").permitAll())
			.addFilterAt(new JWTFilter(jwtUtil, memberClient, newTokenClient),
				UsernamePasswordAuthenticationFilter.class)
			.sessionManagement(session -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();
	}
}

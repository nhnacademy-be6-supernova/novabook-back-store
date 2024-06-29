package store.novabook.store.common.config;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTUtil {
	private final SecretKey secretKey;

	public JWTUtil(@Value("${jwt.secret}") String secret) {
		byte[] keyBytes = Decoders.BASE64.decode(secret);
		this.secretKey = Keys.hmacShaKeyFor(keyBytes);
	}

	public String getUsername(String token) {

		Claims claims = Jwts
			.parserBuilder()
			.setSigningKey(secretKey)
			.build()
			.parseClaimsJws(token)
			.getBody();

		return claims.get("uuid", String.class);

	}

	public String getRole(String token) {

		Claims claims = Jwts
			.parserBuilder()
			.setSigningKey(secretKey)
			.build()
			.parseClaimsJws(token)
			.getBody();

		return claims.get("authorities", String.class);
	}

	public boolean isExpired(String token) {
		Claims claims = null;
		try {
			claims = Jwts
				.parserBuilder()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(token)
				.getBody();
		} catch (ExpiredJwtException e) {
			return true;
		}

		return claims.getExpiration().before(new Date());
	}

	public String getCategory(String token) {

		Claims claims = Jwts
			.parserBuilder()
			.setSigningKey(secretKey)
			.build()
			.parseClaimsJws(token)
			.getBody();

		return claims.get("category", String.class);
	}

	public String createJwt(String category, String username, String role, Long expiredMs) {

		return Jwts.builder()
			.claim("category", category)
			.claim("username", username)
			.claim("role", role)
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + expiredMs))
			.signWith(secretKey)
			.compact();
	}
}

package com.softeer.team6four.global.auth;

import static com.softeer.team6four.global.auth.AuthConst.*;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.softeer.team6four.global.auth.exception.ExpiredTokenException;
import com.softeer.team6four.global.auth.exception.InvalidTokenException;
import com.softeer.team6four.global.auth.exception.JwtException;
import com.softeer.team6four.global.response.ErrorCode;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {
	private final RedisTemplate<String, String> redisTemplate;
	@Value("${jwt.secret}")
	private String secret;
	@Value("${jwt.accessTokenPeriod}")
	private long accessTokenPeriod;
	@Value("${jwt.refreshTokenPeriod}")
	private long refreshTokenPeriod;

	private Key getSecretKey() {
		return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	}

	private String buildAccessToken(Long userId, Date now) {
		return buildToken(now)
			.setExpiration(new Date(now.getTime() + accessTokenPeriod))
			.setSubject(userId.toString())
			.claim(TOKEN_TYPE, ACCESS_TOKEN)
			.compact();
	}

	private String buildRefreshToken(Long userId, Date now) {
		final String refreshToken = buildToken(now)
			.setExpiration(new Date(now.getTime() + refreshTokenPeriod))
			.setSubject(userId.toString())
			.claim(TOKEN_TYPE, REFRESH_TOKEN)
			.compact();
		storeRefreshToken(userId.toString(), refreshToken);
		return refreshToken;
	}

	private void storeRefreshToken(String userId, String refreshToken) {
		redisTemplate.opsForValue().set(
			userId,
			refreshToken,
			refreshTokenPeriod,
			TimeUnit.MILLISECONDS);
	}

	public void removeRefreshToken(Long userId) {
		redisTemplate.delete(userId.toString());
	}

	private JwtBuilder buildToken(Date now) {
		final Key key = getSecretKey();
		return Jwts.builder()
			.setIssuedAt(now)
			.signWith(key);
	}

	public String generateAccessToken(Long userId) {
		final Date now = new Date();
		return buildAccessToken(userId, now);
	}

	public String generateRefreshToken(Long userId) {
		final Date now = new Date();
		return buildRefreshToken(userId, now);
	}

	public void validateToken(String token) {
		JwtParser jwtParser = Jwts.parserBuilder()
			.setSigningKey(getSecretKey())
			.build();
		try {
			jwtParser.parse(token);
		} catch (MalformedJwtException | SignatureException | IllegalArgumentException e) {
			log.error("Invalid Token : {}", e.getMessage());
			throw new InvalidTokenException(ErrorCode.INVALID_TOKEN);
		} catch (ExpiredJwtException e) {
			throw new ExpiredTokenException(ErrorCode.EXPIRED_TOKEN);

		}
	}

	public String extractUserId(String token) {
		String userId;
		try {
			userId = Jwts.parserBuilder()
				.setSigningKey(getSecretKey())
				.build()
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
		} catch (Exception e) {
			throw new JwtException(ErrorCode.INVALID_TOKEN);
		}
		return userId;
	}

	public String reIssue(String refreshToken) {
		Long userId = validateRefreshToken(refreshToken);
		return generateAccessToken(userId);
	}

	public Long validateRefreshToken(String refreshToken) {
		validateToken(refreshToken);
		final Long userId = Long.parseLong(extractUserId(refreshToken));
		final String storedRefreshToken = getRefreshToken(userId.toString());
		if (!Objects.equals(refreshToken, storedRefreshToken)) {
			throw new InvalidTokenException(ErrorCode.INVALID_TOKEN);
		}
		return userId;
	}

	private String getRefreshToken(String userId) {
		return String.valueOf(redisTemplate.opsForValue().get(userId));
	}

}

package com.softeer.team6four.global.filter;

import java.io.IOException;
import java.util.Arrays;

import com.softeer.team6four.global.auth.JwtProvider;
import com.softeer.team6four.global.consts.WhitePathsConst;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtAuthenticationFilter implements Filter {
	private final JwtProvider jwtProvider;

	public JwtAuthenticationFilter(JwtProvider jwtProvider) {
		this.jwtProvider = jwtProvider;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws
		IOException,
		ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		String requestURI = httpRequest.getRequestURI();// 요청 URL

		// 요청 URL 이 허용 목록에 포함되어 있으면 JWT 를 뽑지 않음
		if (Arrays.asList(WhitePathsConst.WHITE_PATHS).contains(requestURI)) {
			chain.doFilter(request, response);
			return;
		}
		// JWT 에서 userId 를 뽑아서 ThreadLocal 에 저장, 이후 진행
		String token = httpRequest.getHeader("Authorization");
		if (token != null && token.startsWith("Bearer ")) {
			token = token.substring(7);
			Long userId = Long.parseLong(jwtProvider.extractUserId(token));
			// ThreadLocal 에 사용자 정보 저장
			UserContextHolder.set(userId);
		}

		try {
			chain.doFilter(request, response);
		} finally {
			// 요청 처리 후 ThreadLocal 정리
			UserContextHolder.clear();
		}
	}
}

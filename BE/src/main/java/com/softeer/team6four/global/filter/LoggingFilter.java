package com.softeer.team6four.global.filter;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggingFilter extends OncePerRequestFilter {
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		// 전처리 과정 - HttpServletRequest 와 HttpServletResponse 를 캐시 가능하도록 래핑해준다.
		ContentCachingRequestWrapper httpServletRequest = new ContentCachingRequestWrapper((HttpServletRequest)request);
		ContentCachingResponseWrapper httpServletResponse = new ContentCachingResponseWrapper(
			(HttpServletResponse)response);

		// 전, 후 처리의 기준이되는 메소드
		// filter 의 동작에 httpServletRequest, httpServletResponse 를 이용한다.
		filterChain.doFilter(httpServletRequest, httpServletResponse);

		// 후 처리 과정

		// request 요청으로 어떤 uri 가 들어왔는지 확인
		String uri = httpServletRequest.getRequestURI();

		//request 내용 확인
		String reqContent = new String(httpServletRequest.getContentAsByteArray());
		log.info("URI: {}, Request: {}", uri, reqContent);

		// response 내용 상태 정보, 내용 확인
		int httpStatus = httpServletResponse.getStatus();
		String resContent = new String(httpServletResponse.getContentAsByteArray());

		// 주의 : response 를 클라이언트에서 볼 수 있도록 하려면 response 를 복사해야 한다. response 를 콘솔에 보여주면 내용이 사라진다.
		httpServletResponse.copyBodyToResponse();

		log.info("Status: {}, Response: {}", httpStatus, resContent);
	}
}

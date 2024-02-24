package com.softeer.team6four.domain;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class HealthCheckController {

	@GetMapping(value = "/health/check")
	public void healthCheck() {
		
	}
}

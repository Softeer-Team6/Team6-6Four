package com.softeer.team6four.domain.common;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.softeer.team6four.domain.carbob.infra.CarbobRepositoryImpl;
import com.softeer.team6four.domain.payment.infra.PaymentRepositoryImpl;
import com.softeer.team6four.domain.reservation.infra.DailyReservationRepositoryImpl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@EnableJpaAuditing
@TestConfiguration
public class TestQueryDslConfig {
	@PersistenceContext
	private EntityManager entityManager;

	@Bean
	public JPAQueryFactory jpaQueryFactory() {
		return new JPAQueryFactory(entityManager);
	}

	@Bean
	public CarbobRepositoryImpl carbobRepositoryImpl() {
		return new CarbobRepositoryImpl(jpaQueryFactory());
	}

	@Bean
	public PaymentRepositoryImpl paymentRepositoryImpl() {
		return new PaymentRepositoryImpl(jpaQueryFactory());
	}
	@Bean
	public DailyReservationRepositoryImpl DailyReservationRepositoryImpl() {
		return new DailyReservationRepositoryImpl(jpaQueryFactory());
	}
}

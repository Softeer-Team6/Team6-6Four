package com.softeer.team6four.domain.payment.infra;

import static com.softeer.team6four.domain.payment.domain.QPayment.*;
import static com.softeer.team6four.domain.reservation.domain.QReservation.*;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.softeer.team6four.domain.payment.application.response.MyPointSummary;
import com.softeer.team6four.domain.payment.domain.PayType;
import com.softeer.team6four.domain.payment.domain.Payment;

@Repository
public class PaymentRepositoryImpl extends QuerydslRepositorySupport {
	private final JPAQueryFactory queryFactory;

	public PaymentRepositoryImpl(JPAQueryFactory queryFactory) {
		super(Payment.class);
		this.queryFactory = queryFactory;
	}

	public Slice<MyPointSummary> findMyPointSummaryList(Long userId, Long lastPaymentId, Pageable pageable) {
		JPAQuery<MyPointSummary> query = queryFactory
			.select(Projections.constructor(
				MyPointSummary.class,
				payment.paymentId,
				payment.amount,
				payment.createdDate,
				payment.payType,
				payment.targetId,
				new CaseBuilder()
					.when(payment.payType.eq(PayType.CHARGE))
					.then("충전")
					.otherwise(reservation.carbob.nickname)
			))
			.from(payment)
			.leftJoin(reservation).on(reservation.reservationId.eq(payment.targetId))
			.where(
				payment.user.userId.eq(userId),
				ltPaymentId(lastPaymentId)
			)
			.orderBy(payment.createdDate.desc());

		List<MyPointSummary> results = query
			.limit(pageable.getPageSize() + 1)
			.fetch();

		return checkLastPage(pageable, results);
	}

	private BooleanExpression ltPaymentId(Long paymentId) {
		if (paymentId == null) {
			return null;
		}
		return payment.paymentId.lt(paymentId);
	}

	private Slice<MyPointSummary> checkLastPage(Pageable pageable, List<MyPointSummary> results) {
		boolean hasNext = false;

		if (results.size() > pageable.getPageSize()) {
			hasNext = true;
			results.remove(pageable.getPageSize());
		}

		return new SliceImpl<>(results, pageable, hasNext);
	}

}

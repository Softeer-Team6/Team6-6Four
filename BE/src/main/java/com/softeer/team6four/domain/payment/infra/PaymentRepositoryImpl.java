package com.softeer.team6four.domain.payment.infra;

import static com.softeer.team6four.domain.payment.domain.QPayment.payment;
import static com.softeer.team6four.domain.reservation.domain.QReservation.reservation;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.softeer.team6four.domain.payment.application.response.MyPointSummary;
import com.softeer.team6four.domain.payment.domain.PayType;
import com.softeer.team6four.domain.payment.domain.Payment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

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
                            Expressions.cases()
                                    .when(payment.payType.eq(PayType.CHARGE)).then(0L) // CHARGE일 땐, 예약정보 0
                                    .otherwise(payment.targetId) // INCOME, USE일땐 예약ID
                                    .as("targetId"),
                            Expressions.cases()
                                    .when(payment.targetId.eq(0L)).then("충전") // CHARGE인 경우 "충전" 문자열 선택
                                    .otherwise(
                                            JPAExpressions.select(reservation.carbob.nickname)
                                                    .from(reservation)
                                                    .where(reservation.reservationId.eq(payment.targetId))
                                                    .limit(1)
                                    )
                        )
                )
                .from(payment)
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

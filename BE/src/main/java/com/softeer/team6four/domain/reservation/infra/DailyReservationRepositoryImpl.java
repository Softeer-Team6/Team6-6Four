package com.softeer.team6four.domain.reservation.infra;

import static com.softeer.team6four.domain.reservation.domain.QReservation.*;
import static com.softeer.team6four.domain.reservation.domain.QReservationLine.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.softeer.team6four.domain.reservation.domain.Reservation;
import com.softeer.team6four.domain.reservation.domain.StateType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class DailyReservationRepositoryImpl extends QuerydslRepositorySupport {
	private final JPAQueryFactory queryFactory;

	public DailyReservationRepositoryImpl(JPAQueryFactory queryFactory) {
		super(Reservation.class);
		this.queryFactory = queryFactory;
	}

	public List<LocalDateTime> findDailyReservationStatus(Long carbobId, LocalDateTime startTime,
		LocalDateTime endTime) {

		BooleanExpression checkDailyImpossibleTime = reservationLine.reservationTime.between(startTime, endTime);

		JPAQuery<LocalDateTime> query = queryFactory
			.select(reservationLine.reservationTime)
			.from(reservation)
			.leftJoin(reservation.reservationLines, reservationLine)
			.where(
				reservation.carbob.carbobId.eq(carbobId),
				reservation.stateType.ne(StateType.REJECT),
				checkDailyImpossibleTime
			);

		return query.fetch();
	}
}


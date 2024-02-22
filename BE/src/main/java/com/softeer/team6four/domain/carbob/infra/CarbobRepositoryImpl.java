package com.softeer.team6four.domain.carbob.infra;

import static com.softeer.team6four.domain.carbob.domain.QCarbob.*;
import static com.softeer.team6four.domain.carbob.domain.QCarbobImage.*;
import static com.softeer.team6four.domain.reservation.domain.QReservation.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.softeer.team6four.domain.carbob.application.response.MyCarbobSummary;
import com.softeer.team6four.domain.carbob.domain.Carbob;
import com.softeer.team6four.domain.carbob.presentation.MyCarbobSortType;
import com.softeer.team6four.domain.reservation.domain.StateType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class CarbobRepositoryImpl extends QuerydslRepositorySupport {
	private final JPAQueryFactory queryFactory;

	public CarbobRepositoryImpl(JPAQueryFactory queryFactory) {
		super(Carbob.class);
		this.queryFactory = queryFactory;
	}

	public Slice<MyCarbobSummary> findCarbobSummaryByUserId(Long userId, MyCarbobSortType sortType, Long lastCarbobId,
		Long lastReservationCount, Pageable pageable) {
		NumberExpression<Long> reservationCount =
			reservation.carbob.carbobId.eq(carbob.carbobId)
				.count();

		JPAQuery<MyCarbobSummary> query = queryFactory
			.select(
				Projections.constructor(
					MyCarbobSummary.class,
					carbob.carbobId,
					carbob.nickname,
					carbob.location.address,
					carbobImage.imageUrl,
					reservationCount
				))
			.from(carbob)
			.leftJoin(carbob.carbobImage, carbobImage)
			.leftJoin(carbob.reservations, reservation).on(reservation.stateType.eq(StateType.WAIT))
			.where(
				carbob.host.userId.eq(userId),
				decideExpression(sortType, lastCarbobId)
			)
			.groupBy(carbob.carbobId, carbobImage.imageUrl);

		if (sortType.equals(MyCarbobSortType.POPULAR)) {
			if (lastCarbobId != null || lastReservationCount != null) {
				query = query
					.having(reservationCount.lt(lastReservationCount)
						.or(reservationCount.eq(lastReservationCount)
							.and(carbob.carbobId.lt(lastCarbobId))));
			}
		}

		query = query
			.orderBy(sortCarbob(sortType, reservationCount).toArray(OrderSpecifier[]::new));

		List<MyCarbobSummary> results = query
			.limit(pageable.getPageSize() + 1)
			.fetch();

		return checkLastPage(pageable, results);
	}

	private BooleanExpression decideExpression(MyCarbobSortType sortType, Long carbobId) {
		if (sortType.equals(MyCarbobSortType.LATEST)) {
			if (carbobId == null)
				return null;
			return ltCarbobId(carbobId);
		} else {
			return null;
		}
	}

	private BooleanExpression ltCarbobId(Long carbobId) {
		if (carbobId == null) {
			return null;
		}
		return carbob.carbobId.lt(carbobId);
	}

	private List<OrderSpecifier<?>> sortCarbob(MyCarbobSortType sort, NumberExpression<Long> reservationCount) {
		List<OrderSpecifier<?>> orders = new ArrayList<>();
		if (sort.equals(MyCarbobSortType.LATEST)) {
			orders.add(carbob.createdDate.desc());
		} else { // 예약 건수가 많은 순
			orders.add(reservationCount.desc());
			orders.add(carbob.createdDate.desc());
		}
		return orders;
	}

	private Slice<MyCarbobSummary> checkLastPage(Pageable pageable, List<MyCarbobSummary> results) {
		boolean hasNext = false;

		if (results.size() > pageable.getPageSize()) {
			hasNext = true;
			results.remove(pageable.getPageSize());
		}

		return new SliceImpl<>(results, pageable, hasNext);
	}
}

package com.softeer.team6four.domain.reservation.infra;

import static com.softeer.team6four.domain.carbob.domain.QCarbob.carbob;
import static com.softeer.team6four.domain.carbob.domain.QCarbobImage.carbobImage;
import static com.softeer.team6four.domain.reservation.domain.QReservation.reservation;
import static com.softeer.team6four.domain.reservation.domain.QReservationLine.reservationLine;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.softeer.team6four.domain.reservation.application.ReservationTime;
import com.softeer.team6four.domain.reservation.application.response.ReservationInfo;
import com.softeer.team6four.domain.reservation.domain.Reservation;
import com.softeer.team6four.domain.reservation.domain.ReservationLine;
import com.softeer.team6four.domain.reservation.domain.StateType;
import com.softeer.team6four.domain.reservation.presentation.ReservationStateSortType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;


@Slf4j
@Repository
public class ReservationRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public ReservationRepositoryImpl(JPAQueryFactory queryFactory) {
        super(Reservation.class);
        this.queryFactory = queryFactory;
    }

    public Slice<ReservationInfo> findReservationInfoList(Long userId, ReservationStateSortType sortType, Long lastReservationId, Pageable pageable) {
        JPAQuery<ReservationInfo> query = queryFactory
            .select(
                Projections.constructor(
                    ReservationInfo.class,
                    reservation.reservationId,
                    carbobImage.imageUrl.coalesce("DEFAULT_IMAGE_URL"),
                    reservation.stateType,
                    Projections.constructor(
                        ReservationTime.class,
                        reservationLine.reservationTime.min().as("startTime"),
                        reservationLine.reservationTime.max().as("endTime")
                    ),
                    carbob.nickname, // reservationTimeStr 을 위한 임시값
                    reservation.totalFee,
                    carbob.nickname.as("carbobNickname"),
                    carbob.location.address
                ))
            .from(reservation)
            .leftJoin(reservation.carbob, carbob)
            .leftJoin(carbob.carbobImage, carbobImage)
            .leftJoin(reservation.reservationLines, reservationLine)
            .where(
                reservation.guest.userId.eq(userId),
                ltReservationId(lastReservationId),
                eqStateType(sortType)
            )
            .groupBy(reservation.reservationId)
            .orderBy(reservation.createdDate.desc());

        List<ReservationInfo> results = query
            .limit(pageable.getPageSize() + 1)
            .fetch();

        return checkLastPage(pageable, results);
    }

    public boolean existsByCarbobIdAndReservationLines(Long carbobId, List<ReservationLine> newReservationLines) {
        // 동적으로 조건을 생성하기 위한 BooleanExpression 리스트
        List<BooleanExpression> timeConditions = new ArrayList<>();

        // reservationTimes 리스트를 순회하면서 각 ReservationTime 에 대한 조건 생성
        for (ReservationLine time : newReservationLines) {
            LocalDateTime newReservationTime = time.getReservationTime();
            BooleanExpression timeCondition = reservation.reservationLines.any().reservationTime.eq(newReservationTime);
            timeConditions.add(timeCondition);
        }

        BooleanExpression combinedCondition = Expressions.FALSE;
        for (BooleanExpression condition : timeConditions) {
            combinedCondition = combinedCondition.or(condition);
        }

        // 겹치는 것이 하나라도 있으면 true, 없으면 false 리턴
        return queryFactory
            .selectOne()
            .from(reservation)
            .where(
                reservation.carbob.carbobId.eq(carbobId),
                reservation.stateType.ne(StateType.REJECT),
                combinedCondition
            )
            .fetchFirst() != null;
    }

    private BooleanExpression ltReservationId(Long reservationId) {
        if (reservationId == null) {
            return null;
        }
        return reservation.reservationId.lt(reservationId);
    }

    private BooleanExpression eqStateType(ReservationStateSortType sortType) {
        if (sortType == ReservationStateSortType.WAIT) {
            return reservation.stateType.eq(StateType.WAIT);
        } else if (sortType == ReservationStateSortType.APPROVE) {
            return reservation.stateType.eq(StateType.APPROVE).or(reservation.stateType.eq(StateType.USED));
        } else if (sortType == ReservationStateSortType.REJECT) {
            return reservation.stateType.eq(StateType.REJECT);
        } else {
            return null;
        }
    }

    private Slice<ReservationInfo> checkLastPage(Pageable pageable, List<ReservationInfo> results) {
        boolean hasNext = false;

        if (results.size() > pageable.getPageSize()) {
            hasNext = true;
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }
}
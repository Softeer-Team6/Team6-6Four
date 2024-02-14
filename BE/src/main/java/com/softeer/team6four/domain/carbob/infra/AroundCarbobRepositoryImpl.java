package com.softeer.team6four.domain.carbob.infra;

import static com.softeer.team6four.domain.carbob.domain.QCarbob.carbob;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.softeer.team6four.domain.carbob.application.response.AroundCarbobListInfo;
import com.softeer.team6four.domain.carbob.application.response.AroundCarbobListInfoSummary;
import com.softeer.team6four.domain.carbob.domain.Carbob;
import java.util.List;

import com.softeer.team6four.domain.carbob.domain.SpeedType;
import com.softeer.team6four.domain.carbob.presentation.CarbobListStateSortType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class AroundCarbobRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public AroundCarbobRepositoryImpl(JPAQueryFactory queryFactory) {
        super(Carbob.class);
        this.queryFactory = queryFactory;
    }

    public List<AroundCarbobListInfoSummary> findAroundCarbobSummaryByMyPosition(Double myLatitude, Double myLongitude) {
        NumberExpression<Double> distance = Expressions.numberTemplate(
                Double.class,
                "6371 * acos(cos(radians({0})) * cos(radians({1})) * cos(radians({2}) - radians({3})) + sin(radians({0})) * sin(radians({1})))",
                myLatitude,
                carbob.location.latitude,
                carbob.location.longitude,
                myLongitude
        );

        JPAQuery<AroundCarbobListInfoSummary> query = queryFactory
                .select(
                        Projections.constructor(
                                AroundCarbobListInfoSummary.class,
                                carbob.carbobId,
                                carbob.info.feePerHour,
                                carbob.location.latitude,
                                carbob.location.longitude
                        )
                )
                .from(carbob)
                .where(distance.lt(5));

        return query.fetch();
    }


    public List<AroundCarbobListInfo> findAroundCarbobByMyPosition
            (Double myLatitude, Double myLongitude, CarbobListStateSortType sortType)
    {

        NumberExpression<Double> distance = Expressions.numberTemplate(
                Double.class,
                "6371 * acos(cos(radians({0})) * cos(radians({1})) * cos(radians({2}) - radians({3})) + sin(radians({0})) * sin(radians({1})))",
                myLatitude,
                carbob.location.latitude,
                carbob.location.longitude,
                myLongitude
        );

        JPAQuery<AroundCarbobListInfo> query = queryFactory
                .select(
                        Projections.constructor(
                                AroundCarbobListInfo.class,
                                carbob.carbobId,
                                carbob.nickname,
                                carbob.location.address,
                                carbob.info.feePerHour,
                                carbob.spec.speedType
                        )
                )
                .from(carbob)
                .where(distance.lt(5));

        switch (sortType) {
            case PRICE:
                query.orderBy(carbob.info.feePerHour.asc());
                break;
            case SPEED:
                // 숫자로 변환하여 정렬
                query.orderBy(
                        new CaseBuilder()
                                .when(carbob.spec.speedType.eq(SpeedType.KWH3)).then(3)
                                .when(carbob.spec.speedType.eq(SpeedType.KWH5)).then(5)
                                .when(carbob.spec.speedType.eq(SpeedType.KWH7)).then(7)
                                .when(carbob.spec.speedType.eq(SpeedType.KWH11)).then(11)
                                .otherwise(0) // 기본값
                                .desc()
                );
                break;
            case DEFAULT_SORT_TYPE:
            default:
                break;
        }

        return query.fetch();
    }

}

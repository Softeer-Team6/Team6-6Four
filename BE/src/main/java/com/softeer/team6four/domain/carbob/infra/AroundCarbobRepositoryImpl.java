package com.softeer.team6four.domain.carbob.infra;

import static com.softeer.team6four.domain.carbob.domain.QCarbob.carbob;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.softeer.team6four.domain.carbob.application.response.AroundCarbobListInfoSummary;
import com.softeer.team6four.domain.carbob.domain.Carbob;
import java.util.List;
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

    public List<AroundCarbobListInfoSummary> findAroundCarbobByMyPosition(Double myLatitude, Double myLongitude) {

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
                                carbob.location.longitude,
                                distance.as("distance")
                        )
                )
                .from(carbob)
                .where(distance.lt(5));

        return query.fetch();
    }
}

package com.softeer.team6four.domain.carbob.infra;

import static com.softeer.team6four.domain.carbob.domain.QCarbob.carbob;
import static com.softeer.team6four.domain.carbob.domain.QCarbobImage.carbobImage;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.softeer.team6four.domain.carbob.application.response.AroundCarbobListInfo;
import com.softeer.team6four.domain.carbob.application.response.AroundCarbobListInfoSummary;
import com.softeer.team6four.domain.carbob.application.response.SpecificDetailCarbobInfo;
import com.softeer.team6four.domain.carbob.domain.*;

import java.util.List;

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
                                buildFeePerHourExpression(carbob.info.feePerHour),
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
                                buildFeePerHourExpression(carbob.info.feePerHour),
                                getSpeedTypeValue(carbob.spec.speedType)
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
                query.orderBy(getSpeedTypeOrder(carbob.spec.speedType));
                break;
        }

        return query.fetch();
    }


    public SpecificDetailCarbobInfo findSpecificCarbobDetailByCarbobId
            (Double myLatitude, Double myLongitude, Long carbobId)
    {

        NumberExpression<Double> distance = Expressions.numberTemplate(
                Double.class,
                "6371 * acos(cos(radians({0})) * cos(radians({1})) * cos(radians({2}) - radians({3})) + sin(radians({0})) * sin(radians({1})))",
                myLatitude,
                carbob.location.latitude,
                carbob.location.longitude,
                myLongitude
        );
        JPAQuery<SpecificDetailCarbobInfo> query = queryFactory
                .select(
                        Projections.constructor(
                                SpecificDetailCarbobInfo.class,
                                carbob.carbobId,
                                carbob.nickname,
                                carbobImage.imageUrl.coalesce("NONE"),
                                distance.as("distance"),
                                carbob.location.address,
                                buildFeePerHourExpression(carbob.info.feePerHour),
                                getChargerTypeValue(carbob.spec.chargerType),
                                getSpeedTypeValue(carbob.spec.speedType),
                                getInstallTypeValue(carbob.spec.installType,carbob.spec.locationType),
                                carbob.info.description
                        )
                )
                .from(carbob)
                .leftJoin(carbob.carbobImage, carbobImage)
                .where(carbob.carbobId.eq(carbobId));

        return query.fetchOne();
    }

    /**
     * 제목 - speedType 가공하는 메서드
     * 설명 - SpeedType에 따라 5kwh, 7kwh, ... AOS에서 바로 쓸 수 있는 데이터를 제공
     *     - DB에 저장된 SpeedType의 code값에 따라 Value를 가져와서 "kwh"과 결합
     * @param - EnumPath<SpeedType> speedTypePath : DB에 저장되어 있는 Enum 값
     * @return - String으로 SpeedType의 value +"kwh"로 반환
     */
    private StringExpression getSpeedTypeValue(EnumPath<SpeedType> speedTypePath) {
        return new CaseBuilder()
                .when(speedTypePath.eq(SpeedType.KWH3)).then(SpeedType.KWH3.getValue()+"kwh")
                .when(speedTypePath.eq(SpeedType.KWH5)).then(SpeedType.KWH5.getValue()+"kwh")
                .when(speedTypePath.eq(SpeedType.KWH7)).then(SpeedType.KWH7.getValue()+"kwh")
                .when(speedTypePath.eq(SpeedType.KWH11)).then(SpeedType.KWH11.getValue()+"kwh")
                .otherwise("");
    }

    /**
     * 제목 - installType 가공하는 메서드
     * 설명 - AOS에서 표시해야하는 값이 "설치형태 : 실내/단독주택" 형식으로 표시해야함
     *     - DB에 저장된 installType code값에 따라 Value를 가져옴
     *     - getLocationTypeValue메서드와 결합하여 가공된 데이터 제공
     * @param - EnumPath<SpeedType> speedTypePath : DB에 저장되어 있는 Enum 값
     *        - EnumPath<LocationType> locationTypePath : DB에 저장되어 있는 Enum 값
     * @return - String으로 speedType의 value+"/"+locationType의 value로 반환
     */
    private StringExpression getInstallTypeValue(EnumPath<InstallType> installTypePath, EnumPath<LocationType> locationTypePath) {
        return new CaseBuilder()
                .when(installTypePath.eq(InstallType.OUTDOOR))
                .then(Expressions.stringTemplate("CONCAT({0}, '/', {1})", InstallType.OUTDOOR.getValue(), getLocationTypeValue(locationTypePath)))
                .when(installTypePath.eq(InstallType.INDOOR))
                .then(Expressions.stringTemplate("CONCAT({0}, '/', {1})", InstallType.INDOOR.getValue(), getLocationTypeValue(locationTypePath)))
                .when(installTypePath.eq(InstallType.CANOPY))
                .then(Expressions.stringTemplate("CONCAT({0}, '/', {1})", InstallType.CANOPY.getValue(), getLocationTypeValue(locationTypePath)))
                .when(installTypePath.eq(InstallType.ETC))
                .then(Expressions.stringTemplate("CONCAT({0}, '/', {1})", InstallType.ETC.getValue(), getLocationTypeValue(locationTypePath)))
                .otherwise("");
    }

    /**
     * 제목 - LocationType 가공하는 메서드
     * 설명 - AOS에서 표시해야하는 값이 "설치형태 : 실내/단독주택" 형식으로 표시해야함
     *     - DB에 저장된 LocationType code값에 따라 Value를 가져옴
     * @param - EnumPath<LocationType> locationTypePath : DB에 저장되어 있는 Enum 값
     * @return - String으로 speedType의 value 반환
     */
    private StringExpression getLocationTypeValue(EnumPath<LocationType> locationTypePath) {
        return new CaseBuilder()
                .when(locationTypePath.eq(LocationType.HOUSE)).then(LocationType.HOUSE.getValue())
                .when(locationTypePath.eq(LocationType.VILLA)).then(LocationType.VILLA.getValue())
                .when(locationTypePath.eq(LocationType.APARTMENT)).then(LocationType.APARTMENT.getValue())
                .when(locationTypePath.eq(LocationType.ETC)).then(LocationType.ETC.getValue())
                .otherwise("");
    }

    /**
     * 제목 - ChargerType 가공하는 메서드
     * 설명 - AOS에서 표시해야하는 값은 Enum의 value이므로 value로 반환해야함
     *     - DB에 저장된 ChargerType code값에 따라 Value를 가져옴
     * @param - EnumPath<LocationType> chargerTypePath : DB에 저장되어 있는 Enum 값
     * @return - String으로 chargerType의 value 반환
     */
    private StringExpression getChargerTypeValue(EnumPath<ChargerType> chargerTypePath) {
        return new CaseBuilder()
                .when(chargerTypePath.eq(ChargerType.SLOW)).then(ChargerType.SLOW.getValue())
                .when(chargerTypePath.eq(ChargerType.AC3)).then(ChargerType.AC3.getValue())
                .when(chargerTypePath.eq(ChargerType.DESTINATION)).then(ChargerType.DESTINATION.getValue())
                .when(chargerTypePath.eq(ChargerType.ETC)).then(ChargerType.ETC.getValue())
                .otherwise("");
    }

    /**
     * 제목 - 속도순으로 정렬하는 메서드
     * 설명 - carbob.spec.speedType는 Enum으로 "KWH3, KWH5, KWH7, KWH11"으로 구성됨
     *     - 해당 값을 desc()로 정렬하면 가장 큰 11이 밑으로 가는 문제가 발생
     *     - 그래서, speedType의 value로 정렬 수행
     * @param - EnumPath<LocationType> speedTypePath : DB에 저장되어 있는 Enum 값
     */
    private OrderSpecifier<Integer> getSpeedTypeOrder(EnumPath<SpeedType> speedTypePath) {
        return new CaseBuilder()
                .when(speedTypePath.eq(SpeedType.KWH3)).then(3)
                .when(speedTypePath.eq(SpeedType.KWH5)).then(5)
                .when(speedTypePath.eq(SpeedType.KWH7)).then(7)
                .when(speedTypePath.eq(SpeedType.KWH11)).then(11)
                .otherwise(0) // 기본값
                .desc();
    }
    /**
     * 제목 - feePerHour를 가공하는 메서드
     * 설명 - AOS에서 표시해야하는 형태는 "100원/kwh"이므로 가공해서 전달
     * @param - NumberPath<Integer> feePerHourPath : DB에 저장되어 있는 int 값
     * @return - String으로 feePerHour+'원/kwh'로 반환
     */
    private StringExpression buildFeePerHourExpression(NumberPath<Integer> feePerHourPath) {
        return Expressions.stringTemplate("CONCAT({0}, '원/kwh')", feePerHourPath.stringValue());
    }
}


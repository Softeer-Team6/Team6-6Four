package com.softeer.team6four.domain.carbob.infra;

import java.util.List;

import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.softeer.team6four.domain.carbob.application.response.AroundCarbobListInfo;
import com.softeer.team6four.domain.carbob.application.response.AroundCarbobListInfoSummary;
import com.softeer.team6four.domain.carbob.application.response.SpecificDetailCarbobInfo;
import com.softeer.team6four.domain.carbob.domain.Carbob;

public interface AroundCarbobRepository extends JpaRepository<Carbob, Long> {

	/**
	 * 제목 - 카밥 반경 5km 조회
	 *
	 * 설명 - 공간 인덱스를 활용하여 기준 위경도를 기준으로 원을 그려 비교한다
	 * @param center
	 * @param radius
	 * @return - List<AroundCarbobListInfoSummary>
	 */
	@Query("""
		SELECT
		    new com.softeer.team6four.domain.carbob.application.response.AroundCarbobListInfoSummary(
		    cb.carbobId,
		    cb.info.feePerHour,
		    ST_X(cb.location.point),
		    ST_Y(cb.location.point)
		    )
		FROM Carbob as cb
		WHERE st_contains(st_buffer(:center, :radius), cb.location.point)
		""")
	List<AroundCarbobListInfoSummary> findAllWithInCircleAreaSummary(final Point center, final int radius);

	/**
	 * 제목 - 카밥 반경 5km 조회(footer)
	 *
	 * 설명 - 공간 인덱스를 활용하여 기준 위경도를 기준으로 원을 그려 비교한다
	 * @param center
	 * @param radius
	 * @return - List<AroundCarbobListInfo>
	 */
	@Query("""
		SELECT
		    new com.softeer.team6four.domain.carbob.application.response.AroundCarbobListInfo(
		    cb.carbobId,
		    cb.nickname,
		    cb.location.address,
		    cb.info.feePerHour,
		    cb.spec.speedType
		    )
		FROM Carbob as cb
		WHERE st_contains(st_buffer(:center, :radius), cb.location.point)
		ORDER BY
		    CASE WHEN :sortType = 'PRICE' THEN cb.info.feePerHour END ASC,
		    CASE WHEN :sortType = 'SPEED' THEN CAST(SUBSTRING(cb.spec.speedType, 4) AS int) END DESC,
		    CASE WHEN :sortType = 'DISTANCE' THEN ST_Distance_Sphere(cb.location.point, :center) END ASC
		""")
	List<AroundCarbobListInfo> findAllWithInCircleArea(final Point center, final int radius, final String sortType);

	/**
	 * 제목 - 특정 카밥을 상세 조회한다.
	 *
	 * 설명 - 특정 카밥의 거리 및 필요한 정보를 반환한다.
	 * @param center
	 * @param carbobId
	 * @return - SpecificDetailCarbobInfo
	 */
	@Query("""
		SELECT
		    new com.softeer.team6four.domain.carbob.application.response.SpecificDetailCarbobInfo(
		    cb.carbobId,
		    cb.nickname,
		    ci.imageUrl,
		    ST_Distance_Sphere(cb.location.point, :center),
		    cb.location.address,
		    cb.info.feePerHour,
		    cb.spec.chargerType,
		    cb.spec.speedType,
		    cb.spec.installType,
		    cb.spec.locationType,
		    cb.info.description
		    )
		FROM Carbob as cb
		LEFT JOIN cb.carbobImage as ci
		WHERE  cb.carbobId = :carbobId
		""")
	SpecificDetailCarbobInfo findSpecificCarbobDetailByCarbobId(final Point center, final Long carbobId);
}

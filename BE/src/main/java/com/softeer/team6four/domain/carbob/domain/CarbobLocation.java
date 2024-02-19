package com.softeer.team6four.domain.carbob.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CarbobLocation {
    @Column(nullable = false)
    private String address;

    @Column(name = "point", nullable = false, columnDefinition = "Point NOT NULL SRID 4326")
    private Point point;

    @Builder
    public CarbobLocation(String address, Double latitude, Double longitude) {
        this.address = address;
        createPoint(latitude,longitude);
    }
    private void createPoint(double latitude, double longitude) {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        this.point = geometryFactory.createPoint(new Coordinate(longitude, latitude));
    }
}

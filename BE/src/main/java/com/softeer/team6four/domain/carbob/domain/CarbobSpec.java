package com.softeer.team6four.domain.carbob.domain;

import com.softeer.team6four.domain.carbob.domain.converter.ChargerTypeConverter;
import com.softeer.team6four.domain.carbob.domain.converter.InstallTypeConverter;
import com.softeer.team6four.domain.carbob.domain.converter.LocationTypeConverter;
import com.softeer.team6four.domain.carbob.domain.converter.SpeedTypeConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CarbobSpec {

    @Convert(converter = LocationTypeConverter.class)
    @Column(name = "location_type", nullable = false)
    private LocationType locationType;

    @Convert(converter = ChargerTypeConverter.class)
    @Column(name = "charger_type", nullable = false)
    private ChargerType chargerType;

    @Convert(converter = SpeedTypeConverter.class)
    @Column(name = "speed_type", nullable = false)
    private SpeedType speedType;

    @Convert(converter = InstallTypeConverter.class)
    @Column(name = "install_type", nullable = false)
    private InstallType installType;

    @Builder
    public CarbobSpec(LocationType locationType, ChargerType chargerType, SpeedType speedType, InstallType installType) {
        this.locationType = locationType;
        this.chargerType = chargerType;
        this.speedType = speedType;
        this.installType = installType;
    }

}

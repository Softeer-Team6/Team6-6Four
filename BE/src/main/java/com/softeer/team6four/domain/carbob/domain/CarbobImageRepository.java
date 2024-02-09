package com.softeer.team6four.domain.carbob.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarbobImageRepository extends JpaRepository<CarbobImage, Long> {
    Optional<CarbobImage> findCarbobImageByCarbob_CarbobId(Long carbobId);
}

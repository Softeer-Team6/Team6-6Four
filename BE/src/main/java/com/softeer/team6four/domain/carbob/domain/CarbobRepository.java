package com.softeer.team6four.domain.carbob.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CarbobRepository extends JpaRepository<Carbob, Long> {
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Carbob c SET c.qrImageUrl = :imageUrl WHERE c.carbobId = :carbobId")
    void updateImageByCarbobId(@Param("carbobId") Long carbobId, @Param("imageUrl") String imageUrl);

}

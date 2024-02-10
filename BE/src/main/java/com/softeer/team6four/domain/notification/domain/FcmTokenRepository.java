package com.softeer.team6four.domain.notification.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {
    List<FcmToken> findAllByUser_UserId(Long userId);
}

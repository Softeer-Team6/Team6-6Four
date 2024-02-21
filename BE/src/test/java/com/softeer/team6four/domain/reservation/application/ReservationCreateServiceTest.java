package com.softeer.team6four.domain.reservation.application;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import com.softeer.team6four.domain.reservation.application.request.ReservationApply;

@SpringBootTest
@ActiveProfiles("local")
@TestPropertySource(locations = "classpath:application-local.yml")
class ReservationCreateServiceTest {
    @Autowired
    private ReservationCreateService reservationCreateService;

    @Test
    @DisplayName("예약 생성 서비스 테스트 V1 - 동시성 검증")
    void makeReservationToCarbobV1() throws InterruptedException {
        // given
        ReservationApply reservationApply = new ReservationApply(4L,
            LocalDateTime.of(2024,2,22,13, 0,0),
            LocalDateTime.of(2024,2,22,17,0,0));

        long startTime = System.currentTimeMillis();

        int numberOfThreads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        List<Future<Long>> futures = new ArrayList<>();

        // when
        for (int i = 0; i < numberOfThreads; i++) {
            Future<Long> future = executorService.submit(() -> {
                try {
                    Long userId = ThreadLocalRandom.current().nextLong(2, 10);
                    return reservationCreateService.makeReservationToCarbobV1(userId, reservationApply);
                } finally {
                    latch.countDown();
                }
            });
            futures.add(future);
        }

        latch.await();

        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime);  // 나노초 단위
         System.out.println("수행 시간: " + duration + " ms");

        // then
        long successfulReservations = futures.stream()
            .filter(future -> {
                try {
                    return future.get() != null; // 성공한 예약(예약 ID가 있는 경우)만 필터링
                } catch (Exception e) {
                    return false;
                }
            })
            .count();

        assertEquals(1, successfulReservations); // 단 하나의 예약만 성공했는지 검증
    }

}

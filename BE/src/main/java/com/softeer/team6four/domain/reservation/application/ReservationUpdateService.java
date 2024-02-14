package com.softeer.team6four.domain.reservation.application;

import com.softeer.team6four.domain.carbob.domain.Carbob;
import com.softeer.team6four.domain.payment.application.exception.PaymentException;
import com.softeer.team6four.domain.payment.domain.PayType;
import com.softeer.team6four.domain.payment.domain.Payment;
import com.softeer.team6four.domain.payment.domain.PaymentRepository;
import com.softeer.team6four.domain.reservation.application.exception.NotApproveReservationException;
import com.softeer.team6four.domain.reservation.application.exception.NotReservationUserException;
import com.softeer.team6four.domain.reservation.application.exception.ReservationNotFoundException;
import com.softeer.team6four.domain.reservation.application.request.ReservationFulfillRequest;
import com.softeer.team6four.domain.reservation.application.response.ReservationFulfillResult;
import com.softeer.team6four.domain.reservation.domain.Reservation;
import com.softeer.team6four.domain.reservation.domain.ReservationLine;
import com.softeer.team6four.domain.reservation.domain.ReservationRepository;
import com.softeer.team6four.domain.reservation.domain.StateType;
import com.softeer.team6four.global.response.ErrorCode;
import com.softeer.team6four.global.response.ResponseDto;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationUpdateService {

    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    public ResponseDto<ReservationFulfillResult> fulfillReservationAndPay(Long userId, ReservationFulfillRequest reservationFulfillRequest) {
        Reservation reservation = reservationRepository.findReservationWithCarbobById(reservationFulfillRequest.getReservationId())
            .orElseThrow(() -> new ReservationNotFoundException(ErrorCode.RESERVATION_NOT_FOUND));
        validateReservation(reservation, userId);

        Long userCurrentTotalPoint = calculateUserCurrentTotalPoint(userId);
        validateUserPoints(reservation, userCurrentTotalPoint);

        Carbob carbob = reservation.getCarbob();
        List<ReservationLine> reservationLines = reservation.getReservationLines();
        String reservationTime = calculateReservationTime(reservationLines);
        String estimatedChargeAmount = calculateEstimatedChargeAmount(carbob, reservationLines.size());

        ReservationFulfillResult reservationFulfillResult = ReservationMapper.mapToReservationFulfillResult
            (carbob, reservation, userCurrentTotalPoint, reservationTime, estimatedChargeAmount);

        updateCarbobUseResult(reservation);

        // TODO : 라즈베리파이 소켓 통신 연동 필요

        return ResponseDto.map(HttpStatus.OK.value(), "카밥 사용이 시작됩니다.", reservationFulfillResult);
    }

    public void updateCarbobUseResult(Reservation reservation) {
        reservation.changeStateType(StateType.USED);
        reservationRepository.save(reservation);

        paymentRepository.save(Payment.builder()
            .amount(-reservation.getTotalFee())
            .targetId(reservation.getReservationId())
            .payType(PayType.USE)
            .user(reservation.getGuest())
            .build());
    }

    private void validateReservation(Reservation reservation, Long userId) {
        if (!reservation.getGuest().getUserId().equals(userId)) {
            throw new NotReservationUserException(ErrorCode.NOT_RESERVATION_USER);
        }
        if (!reservation.getStateType().equals(StateType.APPROVE)) {
            throw new NotApproveReservationException(ErrorCode.RESERVATION_CANNOT_USE_STATE);
        }
    }

    private void validateUserPoints(Reservation reservation, Long userCurrentTotalPoint) {
        if (userCurrentTotalPoint < reservation.getTotalFee()) {
            throw new PaymentException(ErrorCode.INSUFFICIENT_POINTS);
        }
    }

    private Long calculateUserCurrentTotalPoint(Long userId) {
        return paymentRepository.findByUser_UserId(userId).stream()
            .mapToLong(Payment::getAmount)
            .sum();
    }

    private String calculateReservationTime(List<ReservationLine> reservationLines) {
        StringBuilder sb = new StringBuilder();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy.MM.dd");

        String minTime = reservationLines.stream()
            .map(ReservationLine::getReservationTime)
            .min(LocalDateTime::compareTo)
            .orElse(LocalDateTime.MAX).format(formatter);

        String maxTimePlus1Hour = reservationLines.stream()
            .map(ReservationLine::getReservationTime)
            .max(LocalDateTime::compareTo)
            .orElse(LocalDateTime.MIN).plusHours(1).format(formatter);

        return sb.append(minTime).append(" ~ ").append(maxTimePlus1Hour)
            .append(", 총 ").append(reservationLines.size()).append("시간")
            .toString();
    }

    private String calculateEstimatedChargeAmount(Carbob carbob, int totalReservationHours) {
        int chargeSpec = Integer.parseInt(carbob.getSpec().getSpeedType().getValue());
        int estimatedChargeAmount = totalReservationHours * chargeSpec;
        return estimatedChargeAmount + "kW";
    }
}

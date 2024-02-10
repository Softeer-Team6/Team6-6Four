package com.softeer.team6four.domain.notification.application;

import com.softeer.team6four.domain.reservation.infra.ReservationCreatedEvent;
import com.softeer.team6four.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final NotificationCreateService notificationCreateService;
    private final FcmSendService fcmSendService;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleReservationCreatedEvent(ReservationCreatedEvent event) {
        User host = event.getCarbob().getHost();
        User guest = event.getGuset();

        StringBuilder message = new StringBuilder();
        message.append("[ ").append(event.getCarbob().getNickname()).append(" ] 카밥에 ");
        message.append(guest.getNickname()).append(" 님이 예약 요청을 하였습니다.");

        // Notification 생성 로직
        notificationCreateService.createNotification(host, message.toString());
        // FCM 전송 로직
        fcmSendService.sendFcmNotification(host, "예약 요청", message.toString());
    }
}

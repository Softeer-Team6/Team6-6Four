package com.softeer.team6four.domain.payment.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByUser_UserId(Long id);
    List<Payment> findPaymentsByPayTypeAndTargetIdIn(PayType payType, List<Long> targetIdList);
}

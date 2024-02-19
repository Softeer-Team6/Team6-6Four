package com.softeer.team6four.domain.payment.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
	List<Payment> findByUser_UserId(Long id);

	@Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.user.userId = :userId")
	int sumAmountByUserId(Long userId);

	List<Payment> findPaymentsByPayTypeAndTargetIdIn(PayType payType, List<Long> targetIdList);
}

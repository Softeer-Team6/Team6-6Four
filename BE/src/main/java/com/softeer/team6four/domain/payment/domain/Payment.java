package com.softeer.team6four.domain.payment.domain;

import com.softeer.team6four.domain.user.domain.User;
import com.softeer.team6four.global.infrastructure.database.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "payment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "payment_id")
	private Long paymentId;

	@Column(nullable = false)
	private Integer amount;

	@Convert(converter = PayTypeConverter.class)
	@Column(nullable = false)
	private PayType payType;

	/**
	 * 0: 충전 / 1 이상: 예약 id
	 */
	@Column(name = "target_id", nullable = false)
	private Long targetId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Builder
	public Payment(Integer amount, PayType payType, Long targetId, User user) {
		this.amount = amount;
		this.payType = payType;
		this.targetId = targetId;
		this.user = user;
	}

}

package com.softeer.team6four.domain.reservation.domain;

import java.util.List;

import com.softeer.team6four.domain.carbob.domain.Carbob;
import com.softeer.team6four.domain.user.domain.User;
import com.softeer.team6four.global.infrastructure.database.BaseEntity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "reservation")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "reservation_id")
	private Long reservationId;

	@Column(name = "total_fee", nullable = false)
	private Integer totalFee;

	@Convert(converter = StateTypeConverter.class)
	@Column(name = "state_type", nullable = false)
	private StateType stateType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "carbob_id", nullable = false)
	private Carbob carbob;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "guest_id", nullable = false)
	private User guest;

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "reservation_line", joinColumns = @JoinColumn(name = "reservation_id"))
	@OrderColumn(name = "line_idx")
	private List<ReservationLine> reservationLines;

	@Builder
	public Reservation(Integer totalFee, StateType stateType, Carbob carbob, User guest,
		List<ReservationLine> reservationLines) {
		this.totalFee = totalFee;
		this.stateType = stateType;
		this.carbob = carbob;
		this.guest = guest;
		this.reservationLines = reservationLines;
	}
	public void changeStateType(StateType stateType) {
		this.stateType = stateType;
	}
}

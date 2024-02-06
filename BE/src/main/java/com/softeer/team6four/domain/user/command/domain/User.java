package com.softeer.team6four.domain.user.command.domain;

import com.softeer.team6four.domain.carbob.command.domain.Carbob;
import com.softeer.team6four.domain.notification.command.domain.FcmToken;
import com.softeer.team6four.domain.notification.command.domain.Notification;
import com.softeer.team6four.domain.payment.command.domain.Payment;
import com.softeer.team6four.domain.reservation.command.domain.Reservation;
import com.softeer.team6four.global.infrastructure.database.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "carbob_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String nickname;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<FcmToken> fcmTokens;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Notification> notifications;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Payment> payments;

    @OneToMany(mappedBy = "host", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Carbob> carbobs;

    @OneToMany(mappedBy = "guest", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Reservation> reservations;

    @Builder
    public User(Long userId, String email, String password, String nickname) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }
}

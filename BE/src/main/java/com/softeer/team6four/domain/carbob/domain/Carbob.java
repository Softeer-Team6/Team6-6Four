package com.softeer.team6four.domain.carbob.domain;

import com.softeer.team6four.domain.reservation.domain.Reservation;
import com.softeer.team6four.domain.user.domain.User;
import com.softeer.team6four.global.infrastructure.database.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "carbob")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Carbob extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "carbob_id")
    private Long carbobId;

    @Column(nullable = false)
    private String nickname;

    @Embedded
    private CarbobInfo info;

    @Embedded
    private CarbobLocation location;

    @Embedded
    private CarbobSpec spec;

    @OneToOne(mappedBy = "carbob", fetch = FetchType.LAZY)
    private CarbobImage carbobImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id")
    private User host;

    @OneToMany(mappedBy = "carbob", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Reservation> reservations;

    @Builder
    public Carbob(String nickname, CarbobInfo info, CarbobLocation location, CarbobSpec spec) {
        this.nickname = nickname;
        this.info = info;
        this.location = location;
        this.spec = spec;
    }
}

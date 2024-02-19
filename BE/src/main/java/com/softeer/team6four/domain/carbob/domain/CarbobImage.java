package com.softeer.team6four.domain.carbob.domain;

import com.softeer.team6four.global.infrastructure.database.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "carbob_image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CarbobImage extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "carbob_image_id")
	private Long carbobImageId;

	@Column(nullable = false)
	private String imageUrl;

	@OneToOne
	@JoinColumn(name = "carbob_id", nullable = false)
	private Carbob carbob;

	@Builder
	public CarbobImage(String imageUrl, Carbob carbob) {
		this.imageUrl = imageUrl;
		this.carbob = carbob;
	}
}

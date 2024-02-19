package com.softeer.team6four.domain.user.application.response;

import com.google.firebase.database.annotations.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailCheck {
	private final @NotNull boolean emailExists;
}

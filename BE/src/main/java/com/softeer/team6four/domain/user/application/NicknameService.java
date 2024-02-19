package com.softeer.team6four.domain.user.application;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softeer.team6four.domain.user.application.response.NicknameCheck;
import com.softeer.team6four.domain.user.domain.UserRepository;
import com.softeer.team6four.global.response.ResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NicknameService {
	private final UserRepository userRepository;

	public ResponseDto<NicknameCheck> findNickname(String nickname) {
		boolean nicknameExists = userRepository.existsByNickname(nickname);
		NicknameCheck nicknameCheck = new NicknameCheck(nicknameExists);

		if (nicknameExists) {
			return ResponseDto.map(HttpStatus.OK.value(), "중복된 닉네임입니다.", nicknameCheck);
		} else {
			return ResponseDto.map(HttpStatus.OK.value(), "사용 가능한 닉네임입니다.", nicknameCheck);
		}
	}
}

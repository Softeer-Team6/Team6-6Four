package com.softeer.team6four.domain.user.application;

import com.softeer.team6four.domain.user.application.exception.UserNotFoundException;
import com.softeer.team6four.domain.user.application.request.SignInRequest;
import com.softeer.team6four.domain.user.application.request.SignUpRequest;
import com.softeer.team6four.domain.user.application.response.SignInJwtResponse;
import com.softeer.team6four.domain.user.domain.User;
import com.softeer.team6four.domain.user.domain.UserRepository;
import com.softeer.team6four.global.auth.JwtProvider;
import com.softeer.team6four.global.response.ErrorCode;
import com.softeer.team6four.global.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserJoinService {
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @Transactional
    public void signup(SignUpRequest signupRequest) {
        User newUser = User.builder()
                .email(signupRequest.getEmail())
                .password(signupRequest.getPassword())
                .nickname(signupRequest.getNickname())
                .build();
        userRepository.save(newUser);

    }

    @Transactional(readOnly = true)
    public ResponseDto<SignInJwtResponse> signin(SignInRequest signInRequest) {
        User user = userRepository.findUserByEmail(signInRequest.getEmail())
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        String accessToken = jwtProvider.generateAccessToken(user.getUserId());
        String refreshToken = jwtProvider.generateRefreshToken(user.getUserId());

        SignInJwtResponse signInJwtResponse = SignInJwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        return ResponseDto.map(HttpStatus.OK.value(),"로그인에 성공했습니다.", signInJwtResponse);

    }


}

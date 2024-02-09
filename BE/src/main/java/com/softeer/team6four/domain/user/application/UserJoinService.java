package com.softeer.team6four.domain.user.application;

import com.softeer.team6four.domain.user.application.request.SignUpRequest;
import com.softeer.team6four.domain.user.domain.User;
import com.softeer.team6four.domain.user.domain.UserRepository;
import com.softeer.team6four.global.auth.JwtProvider;
import lombok.RequiredArgsConstructor;
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

}

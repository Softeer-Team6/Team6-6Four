package com.softeer.team6four.domain.user.application;

import com.softeer.team6four.domain.user.application.response.EmailCheck;
import com.softeer.team6four.domain.user.domain.UserRepository;
import com.softeer.team6four.global.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EmailService {
    private final UserRepository userRepository;

    public ResponseDto<EmailCheck> findEmail(String email) {
        boolean emailExists = userRepository.existsByEmail(email);
        EmailCheck emailCheck = new EmailCheck(emailExists);

        if (emailExists) {
            return ResponseDto.map(HttpStatus.OK.value(), "중복된 이메일입니다.", emailCheck);
        } else {
            return ResponseDto.map(HttpStatus.OK.value(), "사용 가능한 이메일입니다.", emailCheck);
        }
    }
}

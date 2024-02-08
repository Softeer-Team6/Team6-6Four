package com.softeer.team6four.domain.user.application;

import com.softeer.team6four.domain.user.application.exception.EmailDuplicateException;
import com.softeer.team6four.domain.user.application.response.EmailCheck;
import com.softeer.team6four.domain.user.domain.UserRepository;
import com.softeer.team6four.global.response.ErrorCode;
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
            throw new EmailDuplicateException(ErrorCode.EMAIL_DUPLICATE);
        }
        return ResponseDto.map(HttpStatus.OK.value(),"DB에 없는 이메일이므로 사용가능한 이메일입니다", emailCheck);
    }

}

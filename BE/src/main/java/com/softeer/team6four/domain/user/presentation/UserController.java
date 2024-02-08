package com.softeer.team6four.domain.user.presentation;


import com.softeer.team6four.domain.user.application.EmailService;
import com.softeer.team6four.domain.user.application.response.EmailCheck;
import com.softeer.team6four.global.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final EmailService emailService;

    @GetMapping(value = "/email/check")
    public ResponseDto<EmailCheck> checkEmail(@RequestParam String email) {
        return emailService.findEmail(email);
    }
}


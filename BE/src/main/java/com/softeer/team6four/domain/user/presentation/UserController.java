package com.softeer.team6four.domain.user.presentation;


import com.softeer.team6four.domain.user.application.EmailService;
import com.softeer.team6four.domain.user.application.UserJoinService;
import com.softeer.team6four.domain.user.application.response.EmailCheck;
import com.softeer.team6four.domain.user.application.response.SignUpRequest;
import com.softeer.team6four.global.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final EmailService emailService;
    private final UserJoinService userJoinService;

    @GetMapping(value = "/email/check")
    public ResponseDto<EmailCheck> checkEmail(@RequestParam String email) {
        return emailService.findEmail(email);
    }

    @PostMapping("/signup")
    public void signup(@RequestBody SignUpRequest signupRequest) {userJoinService.signup(signupRequest);}
}


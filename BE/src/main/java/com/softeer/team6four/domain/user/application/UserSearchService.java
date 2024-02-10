package com.softeer.team6four.domain.user.application;

import com.softeer.team6four.domain.user.application.exception.UserException;
import com.softeer.team6four.domain.user.domain.User;
import com.softeer.team6four.domain.user.domain.UserRepository;
import com.softeer.team6four.global.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSearchService {

    private final UserRepository userRepository;

    public User findUserByUserId(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
    }

    public String findUserNicknameByUserId(Long userId){
        User user = userRepository.findById(userId)
                        .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        return user.getNickname();
    }

}

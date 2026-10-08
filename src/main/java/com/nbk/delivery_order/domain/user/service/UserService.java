package com.nbk.delivery_order.domain.user.service;

import com.nbk.delivery_order.domain.user.dto.request.UserSignUpRequestDto;
import com.nbk.delivery_order.domain.user.dto.response.UserResponseDto;
import com.nbk.delivery_order.domain.user.entity.User;
import com.nbk.delivery_order.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDto signUp(UserSignUpRequestDto request) {
        // 아이디 중복 확인
        if (userRepository.existsByLoginId(request.loginId())) {
            throw new IllegalArgumentException("중복된 아이디가 존재합니다.");
        }

        // 사용자 등록
        String encodedPassword = passwordEncoder.encode(request.password());
        User user = User.of(request.loginId(), encodedPassword, request.role());

        return UserResponseDto.from(userRepository.save(user));
    }
}

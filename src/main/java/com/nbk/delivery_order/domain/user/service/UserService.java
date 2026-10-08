package com.nbk.delivery_order.domain.user.service;

import com.nbk.delivery_order.domain.user.dto.request.UserSignUpRequestDto;
import com.nbk.delivery_order.domain.user.dto.response.UserResponseDto;
import com.nbk.delivery_order.domain.user.entity.User;
import com.nbk.delivery_order.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDto signUp(UserSignUpRequestDto request) {
        // 아이디 중복 확인
        if (userRepository.existsByLoginId(request.loginId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "중복된 아이디가 존재합니다.");
        }

        // 사용자 등록
        String encodedPassword = passwordEncoder.encode(request.password());
        User user = User.of(request.loginId(), encodedPassword, request.role());

        return UserResponseDto.from(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public UserResponseDto getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."));

        return UserResponseDto.from(user);
    }
}

package com.nbk.delivery_order.domain.auth.service;

import com.nbk.delivery_order.domain.auth.dto.request.LoginRequestDto;
import com.nbk.delivery_order.domain.auth.dto.response.LoginResponseDto;
import com.nbk.delivery_order.domain.user.entity.User;
import com.nbk.delivery_order.domain.user.repository.UserRepository;
import com.nbk.delivery_order.global.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional(readOnly = true)
    public LoginResponseDto login(LoginRequestDto request) {
        // 아이디가 없는 경우와 비밀번호가 틀린 경우를 구분하지 않음 (아이디 존재 여부 노출 방지)
        User user = userRepository.findByLoginId(request.loginId())
                .filter(found -> passwordEncoder.matches(request.password(), found.getPassword()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 올바르지 않습니다."));

        return new LoginResponseDto(jwtProvider.createToken(user.getId(), user.getRole()));
    }
}

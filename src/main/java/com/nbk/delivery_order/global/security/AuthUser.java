package com.nbk.delivery_order.global.security;

import com.nbk.delivery_order.domain.user.entity.Role;

// 토큰에서 꺼낸 로그인 사용자 정보 (@AuthenticationPrincipal로 주입)
public record AuthUser(
        Long userId,
        Role role
) {
}

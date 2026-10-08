package com.nbk.delivery_order.domain.user.dto.response;

import com.nbk.delivery_order.domain.user.entity.Role;
import com.nbk.delivery_order.domain.user.entity.User;

public record UserResponseDto(
        Long id,
        String loginId,
        Role role
) {

    public static UserResponseDto from(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getLoginId(),
                user.getRole()
        );
    }
}

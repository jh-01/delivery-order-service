package com.nbk.delivery_order.domain.user.dto.response;

import com.nbk.delivery_order.domain.user.entity.Role;
import com.nbk.delivery_order.domain.user.entity.User;

public record UserResponse(
        Long id,
        String loginId,
        Role role
) {

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getLoginId(),
                user.getRole()
        );
    }
}

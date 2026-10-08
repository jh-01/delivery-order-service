package com.nbk.delivery_order.domain.menu.dto.request;

import com.nbk.delivery_order.domain.user.entity.Role;
import com.nbk.delivery_order.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MenuCreateRequestDto(
        @NotBlank(message = "아이디는 필수입니다.")
        @Size(min = 4, max = 20, message = "아이디는 4~20자여야 합니다.")
        User owner,

        @NotBlank(message = "비밀번호는 필수입니다.")
        @Size(min = 8, max = 20, message = "비밀번호는 8~20자여야 합니다.")
        String name,

        @NotBlank(message = "비밀번호는 필수입니다.")
        Long price,

        String description,

        @NotNull(message = "회원 유형은 필수입니다.")
        Role role
) {
}

package com.nbk.delivery_order.domain.user.dto.request;

import com.nbk.delivery_order.domain.user.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserSignUpRequestDto(

        @NotBlank(message = "아이디는 필수입니다.")
        @Size(min = 4, max = 20, message = "아이디는 4~20자여야 합니다.")
        String loginId,

        @NotBlank(message = "비밀번호는 필수입니다.")
        // BCrypt는 72바이트까지만 해싱하므로 상한을 둠
        @Size(min = 8, max = 72, message = "비밀번호는 8자 이상 72자 이하여야 합니다.")
        String password,

        @NotNull(message = "회원 유형은 필수입니다.")
        Role role
) {
}

package com.nbk.delivery_order.domain.order.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderCreateRequestDto(

        @NotNull(message = "고객 ID는 필수입니다.")
        Long customerId,

        @NotBlank(message = "배송 주소는 필수입니다.")
        String deliveryAddress,

        @NotNull(message = "총 금액은 필수입니다.")
        @Positive(message = "총 금액은 0보다 커야 합니다.")
        Long totalPrice
) {
}
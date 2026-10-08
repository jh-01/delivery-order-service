package com.nbk.delivery_order.domain.order.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

public record OrderCreateRequestDto(

        @NotBlank(message = "배송 주소는 필수입니다.")
        @Size(max = 300, message = "배송 주소는 300자 이하여야 합니다.")
        String deliveryAddress,

        @NotEmpty(message = "주문 메뉴는 1개 이상이어야 합니다.")
        List<@Valid OrderMenuRequest> orderMenus
) {
    public record OrderMenuRequest(
            @NotNull(message = "메뉴 ID는 필수입니다.")
            Long menuId,

            @NotNull(message = "수량은 필수입니다.")
            @Positive(message = "수량은 0보다 커야 합니다.")
            Long quantity
    ) {
    }
}

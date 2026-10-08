package com.nbk.delivery_order.domain.order.dto.request;

import com.nbk.delivery_order.domain.order.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record OrderStatusUpdateRequestDto(

        @NotNull(message = "변경할 주문 상태는 필수입니다.")
        OrderStatus status
) {
}

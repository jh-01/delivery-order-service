package com.nbk.delivery_order.domain.payment.dto.request;

import com.nbk.delivery_order.domain.payment.entity.PaymentMethod;
import jakarta.validation.constraints.NotNull;

public record PaymentCreateRequestDto(

        @NotNull(message = "주문 ID는 필수입니다.")
        Long orderId,

        @NotNull(message = "결제 수단은 필수입니다.")
        PaymentMethod paymentMethod
) {
}

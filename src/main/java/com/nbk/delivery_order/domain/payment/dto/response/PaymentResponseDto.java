package com.nbk.delivery_order.domain.payment.dto.response;

import com.nbk.delivery_order.domain.payment.entity.PaymentMethod;
import com.nbk.delivery_order.domain.payment.entity.PaymentStatus;

import java.time.LocalDateTime;

public record PaymentResponseDto(
        Long id,
        Long orderId,
        Long amount,
        PaymentMethod paymentMethod,
        PaymentStatus status,
        LocalDateTime paidAt
) {
}

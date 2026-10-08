package com.nbk.delivery_order.domain.payment.controller;

import com.nbk.delivery_order.domain.payment.dto.request.PaymentCreateRequestDto;
import com.nbk.delivery_order.domain.payment.dto.response.PaymentResponseDto;
import com.nbk.delivery_order.domain.payment.service.PaymentService;
import com.nbk.delivery_order.global.security.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// 주문별 결제 이력(/api/orders/{orderId}/payments)도 함께 다루기 위해 /api 기준으로 매핑
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/payments")
    public ResponseEntity<PaymentResponseDto> createPayment(@AuthenticationPrincipal AuthUser authUser,
                                                            @Valid @RequestBody PaymentCreateRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.createPayment(authUser.userId(), request));
    }

    @GetMapping("/payments/{paymentId}")
    public ResponseEntity<PaymentResponseDto> getPayment(@PathVariable Long paymentId,
                                                         @AuthenticationPrincipal AuthUser authUser) {
        return ResponseEntity.ok(paymentService.getPayment(paymentId, authUser.userId()));
    }

    @GetMapping("/orders/{orderId}/payments")
    public ResponseEntity<List<PaymentResponseDto>> getPaymentsByOrder(@PathVariable Long orderId,
                                                                       @AuthenticationPrincipal AuthUser authUser) {
        return ResponseEntity.ok(paymentService.getPaymentsByOrder(orderId, authUser.userId()));
    }
}

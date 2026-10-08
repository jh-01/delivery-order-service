package com.nbk.delivery_order.domain.payment.controller;

import com.nbk.delivery_order.domain.payment.dto.request.PaymentCreateRequestDto;
import com.nbk.delivery_order.domain.payment.dto.response.PaymentResponseDto;
import com.nbk.delivery_order.domain.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// 주문별 결제 이력(/api/orders/{orderId}/payments)도 함께 다루기 위해 /api 기준으로 매핑
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    // TODO: 로그인 구현 후 userId 대신 인증 정보 사용
    @PostMapping("/payments")
    public ResponseEntity<PaymentResponseDto> createPayment(@RequestParam Long userId,
                                                            @Valid @RequestBody PaymentCreateRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.createPayment(userId, request));
    }

    // TODO: 로그인 구현 후 userId 대신 인증 정보 사용
    @GetMapping("/payments/{paymentId}")
    public ResponseEntity<PaymentResponseDto> getPayment(@PathVariable Long paymentId, @RequestParam Long userId) {
        return ResponseEntity.ok(paymentService.getPayment(paymentId, userId));
    }

    // TODO: 로그인 구현 후 userId 대신 인증 정보 사용
    @GetMapping("/orders/{orderId}/payments")
    public ResponseEntity<List<PaymentResponseDto>> getPaymentsByOrder(@PathVariable Long orderId,
                                                                       @RequestParam Long userId) {
        return ResponseEntity.ok(paymentService.getPaymentsByOrder(orderId, userId));
    }

    // TODO: 로그인 구현 후 userId 대신 인증 정보 사용
    @PatchMapping("/payments/{paymentId}/cancel")
    public ResponseEntity<Void> cancelPayment(@PathVariable Long paymentId, @RequestParam Long userId) {
        paymentService.cancelPayment(paymentId, userId);
        return ResponseEntity.noContent().build();
    }
}

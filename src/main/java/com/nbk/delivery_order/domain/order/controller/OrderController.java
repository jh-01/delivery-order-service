package com.nbk.delivery_order.domain.order.controller;

import com.nbk.delivery_order.domain.order.dto.request.OrderCreateRequestDto;
import com.nbk.delivery_order.domain.order.dto.request.OrderStatusUpdateRequestDto;
import com.nbk.delivery_order.domain.order.dto.response.OrderResponseDto;
import com.nbk.delivery_order.domain.order.service.OrderService;
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

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@Valid @RequestBody OrderCreateRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(request));
    }

    // TODO: 로그인 구현 후 userId 대신 인증 정보 사용
    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getOrders(@RequestParam Long userId) {
        return ResponseEntity.ok(orderService.getOrders(userId));
    }

    // TODO: 로그인 구현 후 userId 대신 인증 정보 사용
    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId, @RequestParam Long userId) {
        orderService.cancelOrder(orderId, userId);
        return ResponseEntity.noContent().build();
    }

    // TODO: 로그인 구현 후 userId 대신 인증 정보 사용
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<Void> updateOrderStatus(@PathVariable Long orderId, @RequestParam Long userId,
                                                  @Valid @RequestBody OrderStatusUpdateRequestDto request) {
        orderService.updateOrderStatus(orderId, userId, request);
        return ResponseEntity.noContent().build();
    }
}

package com.nbk.delivery_order.domain.order.controller;

import com.nbk.delivery_order.domain.order.dto.request.OrderCreateRequestDto;
import com.nbk.delivery_order.domain.order.dto.request.OrderStatusUpdateRequestDto;
import com.nbk.delivery_order.domain.order.dto.response.OrderResponseDto;
import com.nbk.delivery_order.domain.order.service.OrderService;
import com.nbk.delivery_order.global.security.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@AuthenticationPrincipal AuthUser authUser,
                                                        @Valid @RequestBody OrderCreateRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(authUser.userId(), request));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getOrders(@AuthenticationPrincipal AuthUser authUser) {
        return ResponseEntity.ok(orderService.getOrders(authUser.userId()));
    }

    // 주문 취소(고객)와 상태 변경(사장님)을 함께 처리
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<Void> updateOrderStatus(@PathVariable Long orderId, @AuthenticationPrincipal AuthUser authUser,
                                                  @Valid @RequestBody OrderStatusUpdateRequestDto request) {
        orderService.updateOrderStatus(orderId, authUser.userId(), request);
        return ResponseEntity.noContent().build();
    }
}

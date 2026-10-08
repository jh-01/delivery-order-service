package com.nbk.delivery_order.domain.order.dto.response;

import com.nbk.delivery_order.domain.order.entity.Order;
import com.nbk.delivery_order.domain.order.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderResponseDto {

    private Long id;
    private Long customerId;
    private String deliveryAddress;
    private Long totalPrice;
    private OrderStatus status;

    public static OrderResponseDto from(Order order) {
        return new OrderResponseDto(
                order.getId(),
                order.getCustomer().getId(),
                order.getDeliveryAddress(),
                order.getTotalPrice(),
                order.getStatus()
        );
    }
}
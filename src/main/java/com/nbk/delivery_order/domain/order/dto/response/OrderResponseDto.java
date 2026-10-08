package com.nbk.delivery_order.domain.order.dto.response;

import com.nbk.delivery_order.domain.order.entity.Order;
import com.nbk.delivery_order.domain.order.entity.OrderMenu;
import com.nbk.delivery_order.domain.order.entity.OrderStatus;

import java.util.List;

public record OrderResponseDto(
        Long id,
        Long customerId,
        String deliveryAddress,
        Long totalPrice,
        OrderStatus status,
        List<OrderMenuResponse> orderMenus
) {
    public static OrderResponseDto from(Order order, List<OrderMenu> orderMenus) {
        return new OrderResponseDto(
                order.getId(),
                order.getCustomer().getId(),
                order.getDeliveryAddress(),
                order.getTotalPrice(),
                order.getStatus(),
                orderMenus.stream()
                        .map(OrderMenuResponse::from)
                        .toList()
        );
    }

    public record OrderMenuResponse(
            Long menuId,
            String menuName,
            Long quantity
    ) {
        public static OrderMenuResponse from(OrderMenu orderMenu) {
            return new OrderMenuResponse(
                    orderMenu.getMenu().getId(),
                    orderMenu.getMenu().getName(),
                    orderMenu.getQuantity()
            );
        }
    }
}

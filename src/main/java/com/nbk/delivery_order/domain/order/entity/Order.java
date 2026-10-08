package com.nbk.delivery_order.domain.order.entity;

import com.nbk.delivery_order.domain.user.entity.User;
import com.nbk.delivery_order.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User customer;

    @Column(name = "delivery_address", nullable = false, length = 300)
    private String deliveryAddress;

    @Column(name = "total_price", nullable = false)
    private Long totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private OrderStatus status;

    private Order(User customer, String deliveryAddress, Long totalPrice){
        this.customer = customer;
        this.deliveryAddress = deliveryAddress;
        this.totalPrice = totalPrice;
        this.status = OrderStatus.ORDER_REQUESTED;
    }

    public static Order of(User customer, String deliveryAddress, Long totalPrice) {
        return new Order(customer, deliveryAddress, totalPrice);
    }
}

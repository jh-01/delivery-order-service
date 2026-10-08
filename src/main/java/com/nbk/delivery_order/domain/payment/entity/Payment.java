package com.nbk.delivery_order.domain.payment.entity;

import com.nbk.delivery_order.domain.order.entity.Order;
import com.nbk.delivery_order.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 주문 하나에 결제는 한 번만 가능 (동시 결제 요청도 DB unique 제약으로 차단)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @Column(nullable = false)
    private Long amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false, length = 30)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PaymentStatus status;

    @Column(name = "paid_at", nullable = false)
    private LocalDateTime paidAt;

    private Payment(Order order, PaymentMethod paymentMethod) {
        this.order = order;
        this.amount = order.getTotalPrice();
        this.paymentMethod = paymentMethod;
        this.status = PaymentStatus.COMPLETED;
        this.paidAt = LocalDateTime.now();
    }

    // 결제 금액은 주문의 총 가격으로 결정
    public static Payment of(Order order, PaymentMethod paymentMethod) {
        return new Payment(order, paymentMethod);
    }
}

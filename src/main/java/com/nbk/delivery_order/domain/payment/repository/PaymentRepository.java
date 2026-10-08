package com.nbk.delivery_order.domain.payment.repository;

import com.nbk.delivery_order.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // 주문의 결제 이력 (결제한 순서대로)
    List<Payment> findAllByOrder_IdOrderByIdAsc(Long orderId);
}

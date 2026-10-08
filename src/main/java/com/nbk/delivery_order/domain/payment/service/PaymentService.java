package com.nbk.delivery_order.domain.payment.service;

import com.nbk.delivery_order.domain.order.repository.OrderRepository;
import com.nbk.delivery_order.domain.payment.repository.PaymentRepository;
import com.nbk.delivery_order.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
}

package com.nbk.delivery_order.domain.payment.service;

import com.nbk.delivery_order.domain.order.entity.Order;
import com.nbk.delivery_order.domain.order.repository.OrderMenuRepository;
import com.nbk.delivery_order.domain.order.repository.OrderRepository;
import com.nbk.delivery_order.domain.payment.dto.request.PaymentCreateRequestDto;
import com.nbk.delivery_order.domain.payment.dto.response.PaymentResponseDto;
import com.nbk.delivery_order.domain.payment.entity.Payment;
import com.nbk.delivery_order.domain.payment.repository.PaymentRepository;
import com.nbk.delivery_order.domain.user.entity.Role;
import com.nbk.delivery_order.domain.user.entity.User;
import com.nbk.delivery_order.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final OrderMenuRepository orderMenuRepository;
    private final UserRepository userRepository;

    @Transactional
    public PaymentResponseDto createPayment(Long userId, PaymentCreateRequestDto request) {
        User user = findUser(userId);
        Order order = findOrder(request.orderId());

        // 고객 본인 주문만 결제 가능
        if (!isOrderCustomer(user, order)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인 주문만 결제할 수 있습니다.");
        }

        if (!order.isPayable()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "주문요청 상태에서만 결제할 수 있습니다.");
        }

        Payment payment = paymentRepository.save(Payment.of(order, request.paymentMethod()));
        order.completePayment();

        return PaymentResponseDto.from(payment);
    }

    @Transactional(readOnly = true)
    public PaymentResponseDto getPayment(Long paymentId, Long userId) {
        User user = findUser(userId);
        Payment payment = findPayment(paymentId);

        validateReadable(user, payment.getOrder());

        return PaymentResponseDto.from(payment);
    }

    @Transactional(readOnly = true)
    public List<PaymentResponseDto> getPaymentsByOrder(Long orderId, Long userId) {
        User user = findUser(userId);
        Order order = findOrder(orderId);

        validateReadable(user, order);

        return paymentRepository.findAllByOrder_IdOrderByIdAsc(orderId).stream()
                .map(PaymentResponseDto::from)
                .toList();
    }

    // 고객은 본인 주문, 사장님은 본인 메뉴가 들어간 주문의 결제만 조회 가능
    private void validateReadable(User user, Order order) {
        boolean readable = switch (user.getRole()) {
            case CUSTOMER -> isOrderCustomer(user, order);
            case OWNER -> orderMenuRepository.existsByOrder_IdAndMenu_Owner_Id(order.getId(), user.getId());
        };

        if (!readable) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "조회 권한이 없는 결제입니다.");
        }
    }

    private boolean isOrderCustomer(User user, Order order) {
        return user.getRole() == Role.CUSTOMER && order.getCustomer().getId().equals(user.getId());
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."));
    }

    private Order findOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 주문입니다."));
    }

    private Payment findPayment(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 결제입니다."));
    }
}

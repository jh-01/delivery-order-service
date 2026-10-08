package com.nbk.delivery_order.domain.order.service;

import com.nbk.delivery_order.domain.menu.entity.Menu;
import com.nbk.delivery_order.domain.menu.repository.MenuRepository;
import com.nbk.delivery_order.domain.order.dto.request.OrderCreateRequestDto;
import com.nbk.delivery_order.domain.order.dto.request.OrderCreateRequestDto.OrderMenuRequest;
import com.nbk.delivery_order.domain.order.dto.response.OrderResponseDto;
import com.nbk.delivery_order.domain.order.entity.Order;
import com.nbk.delivery_order.domain.order.entity.OrderMenu;
import com.nbk.delivery_order.domain.order.repository.OrderMenuRepository;
import com.nbk.delivery_order.domain.order.repository.OrderRepository;
import com.nbk.delivery_order.domain.user.entity.Role;
import com.nbk.delivery_order.domain.user.entity.User;
import com.nbk.delivery_order.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMenuRepository orderMenuRepository;
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;

    @Transactional
    public OrderResponseDto createOrder(OrderCreateRequestDto request) {
        // TODO: 로그인 구현 후 인증 정보에서 고객 조회
        User customer = userRepository.findById(request.customerId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        // 고객만 주문 가능
        if (customer.getRole() != Role.CUSTOMER) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        // 총 가격은 서버에서 메뉴 가격 기준으로 계산
        long totalPrice = 0;
        List<Menu> menus = new ArrayList<>();
        for (OrderMenuRequest orderMenu : request.orderMenus()) {
            Menu menu = menuRepository.findByIdAndDeletedFalse(orderMenu.menuId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴입니다."));
            totalPrice += menu.getPrice() * orderMenu.quantity();
            menus.add(menu);
        }

        Order order = orderRepository.save(Order.of(customer, request.deliveryAddress(), totalPrice));

        List<OrderMenu> orderMenus = new ArrayList<>();
        for (int i = 0; i < menus.size(); i++) {
            orderMenus.add(OrderMenu.of(order, menus.get(i), request.orderMenus().get(i).quantity()));
        }
        orderMenuRepository.saveAll(orderMenus);

        return OrderResponseDto.from(order, orderMenus);
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDto> getOrders(Long userId) {
        // TODO: 로그인 구현 후 인증 정보에서 회원 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        // 고객은 본인 주문, 사장님은 본인 메뉴가 들어간 주문
        List<OrderMenu> orderMenus = switch (user.getRole()) {
            case CUSTOMER -> orderMenuRepository.findAllByOrder_Customer_IdOrderByOrder_IdDesc(user.getId());
            case OWNER -> orderMenuRepository.findAllByMenu_Owner_IdOrderByOrder_IdDesc(user.getId());
        };

        // 주문 메뉴를 주문 단위로 묶기 (최신 주문 순서 유지)
        Map<Order, List<OrderMenu>> orderMenusByOrder = orderMenus.stream()
                .collect(Collectors.groupingBy(OrderMenu::getOrder, LinkedHashMap::new, Collectors.toList()));

        return orderMenusByOrder.entrySet().stream()
                .map(entry -> OrderResponseDto.from(entry.getKey(), entry.getValue()))
                .toList();
    }

    @Transactional
    public void cancelOrder(Long orderId, Long userId) {
        // TODO: 로그인 구현 후 인증 정보에서 회원 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 주문입니다."));

        // 고객 본인 주문만 취소 가능
        if (user.getRole() != Role.CUSTOMER || !order.getCustomer().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인 주문만 취소할 수 있습니다.");
        }

        if (!order.isCancelable()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "주문요청 상태에서만 취소할 수 있습니다.");
        }

        order.cancel();
    }
}

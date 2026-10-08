package com.nbk.delivery_order.domain.order.repository;

import com.nbk.delivery_order.domain.order.entity.OrderMenu;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderMenuRepository extends JpaRepository<OrderMenu, Long> {

    // 고객 본인이 한 주문의 주문 메뉴
    @EntityGraph(attributePaths = {"order", "menu"})
    List<OrderMenu> findAllByOrder_Customer_IdOrderByOrder_IdDesc(Long customerId);

    // 사장님 본인 메뉴가 들어간 주문 메뉴
    @EntityGraph(attributePaths = {"order", "menu"})
    List<OrderMenu> findAllByMenu_Owner_IdOrderByOrder_IdDesc(Long ownerId);

    // 주문에 사장님 본인 메뉴가 들어있는지 여부
    boolean existsByOrder_IdAndMenu_Owner_Id(Long orderId, Long ownerId);
}

package com.nbk.delivery_order.domain.menu.repository;

import com.nbk.delivery_order.domain.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findAllByDeletedFalse();

    Optional<Menu> findByIdAndDeletedFalse(Long id);
}

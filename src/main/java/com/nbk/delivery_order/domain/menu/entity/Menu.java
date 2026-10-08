package com.nbk.delivery_order.domain.menu.entity;

import com.nbk.delivery_order.domain.user.entity.User;
import com.nbk.delivery_order.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "menus")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private Long price;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private boolean deleted = false;

    private Menu(User owner, String name, Long price, String description){
        this.owner = owner;
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public static Menu of(User owner, String name, Long price, String description){
        return new Menu(owner, name, price, description);
    }
}
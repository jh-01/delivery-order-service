package com.nbk.delivery_order.domain.user.entity;

import com.nbk.delivery_order.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login_id", nullable = false, unique = true, length = 20)
    private String loginId;

    @Column(nullable = false, length = 500)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    private User(String loginId, String password, Role role){
        this.loginId = loginId;
        this.password = password;
        this.role = role;
    }

    public static User of(String loginId, String password, Role role){
        return new User(loginId, password, role);
    }
}
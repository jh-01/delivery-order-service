package com.nbk.delivery_order.domain.menu.service;

import com.nbk.delivery_order.domain.menu.dto.request.MenuCreateRequestDto;
import com.nbk.delivery_order.domain.menu.dto.response.MenuResponse;
import com.nbk.delivery_order.domain.menu.entity.Menu;
import com.nbk.delivery_order.domain.menu.repository.MenuRepository;
import com.nbk.delivery_order.domain.user.entity.Role;
import com.nbk.delivery_order.domain.user.entity.User;
import com.nbk.delivery_order.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final UserRepository userRepository;

    @Transactional
    public MenuResponse addMenu(MenuCreateRequestDto request){
        User owner = userRepository.findById(request.ownerId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        // 사장님만 메뉴 등록 가능
        if (owner.getRole() != Role.OWNER) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        Menu menu = Menu.of(owner, request.name(), request.price(), request.description());

        return MenuResponse.from(menuRepository.save(menu));
    }
}

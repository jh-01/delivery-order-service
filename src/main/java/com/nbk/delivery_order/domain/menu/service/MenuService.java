package com.nbk.delivery_order.domain.menu.service;

import com.nbk.delivery_order.domain.menu.dto.request.MenuCreateRequestDto;
import com.nbk.delivery_order.domain.menu.dto.request.MenuUpdateRequestDto;
import com.nbk.delivery_order.domain.menu.dto.response.MenuResponse;
import com.nbk.delivery_order.domain.menu.entity.Menu;
import com.nbk.delivery_order.domain.menu.repository.MenuRepository;
import com.nbk.delivery_order.domain.user.entity.Role;
import com.nbk.delivery_order.domain.user.entity.User;
import com.nbk.delivery_order.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Transactional(readOnly = true)
    public List<MenuResponse> getMenus() {
        return menuRepository.findAllByDeletedFalse().stream()
                .map(MenuResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public MenuResponse getMenu(Long menuId) {
        Menu menu = menuRepository.findByIdAndDeletedFalse(menuId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴입니다."));

        return MenuResponse.from(menu);
    }

    @Transactional
    public MenuResponse updateMenu(Long menuId, MenuUpdateRequestDto request){
        Menu menu = menuRepository.findByIdAndDeletedFalse(menuId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴입니다."));

        // TODO: 로그인 구현 후 메뉴 소유자만 수정 가능하도록 권한 확인
        menu.update(request.name(), request.price(), request.description());

        return MenuResponse.from(menu);
    }
}

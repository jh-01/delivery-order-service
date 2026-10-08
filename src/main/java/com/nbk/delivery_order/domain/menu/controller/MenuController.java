package com.nbk.delivery_order.domain.menu.controller;

import com.nbk.delivery_order.domain.menu.dto.request.MenuCreateRequestDto;
import com.nbk.delivery_order.domain.menu.dto.request.MenuUpdateRequestDto;
import com.nbk.delivery_order.domain.menu.dto.response.MenuResponse;
import com.nbk.delivery_order.domain.menu.service.MenuService;
import com.nbk.delivery_order.global.security.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;

    @PostMapping
    public ResponseEntity<MenuResponse> addMenu(@AuthenticationPrincipal AuthUser authUser,
                                                @Valid @RequestBody MenuCreateRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(menuService.addMenu(authUser.userId(), request));
    }

    @GetMapping
    public ResponseEntity<List<MenuResponse>> getMenus(){
        return ResponseEntity.ok(menuService.getMenus());
    }

    @GetMapping("/{menuId}")
    public ResponseEntity<MenuResponse> getMenu(@PathVariable Long menuId){
        return ResponseEntity.ok(menuService.getMenu(menuId));
    }

    @PatchMapping("/{menuId}")
    public ResponseEntity<MenuResponse> updateMenu(@PathVariable Long menuId, @AuthenticationPrincipal AuthUser authUser,
                                                   @Valid @RequestBody MenuUpdateRequestDto request){
        return ResponseEntity.ok(menuService.updateMenu(menuId, authUser.userId(), request));
    }

    @DeleteMapping("/{menuId}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long menuId, @AuthenticationPrincipal AuthUser authUser){
        menuService.deleteMenu(menuId, authUser.userId());
        return ResponseEntity.noContent().build();
    }
}

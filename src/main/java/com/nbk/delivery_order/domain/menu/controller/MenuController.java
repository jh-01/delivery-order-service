package com.nbk.delivery_order.domain.menu.controller;

import com.nbk.delivery_order.domain.menu.dto.request.MenuCreateRequestDto;
import com.nbk.delivery_order.domain.menu.dto.request.MenuUpdateRequestDto;
import com.nbk.delivery_order.domain.menu.dto.response.MenuResponse;
import com.nbk.delivery_order.domain.menu.service.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;

    @PostMapping
    public ResponseEntity<MenuResponse> addMenu(@Valid @RequestBody MenuCreateRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(menuService.addMenu(request));
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
    public ResponseEntity<MenuResponse> updateMenu(@PathVariable Long menuId, @Valid @RequestBody MenuUpdateRequestDto request){
        return ResponseEntity.ok(menuService.updateMenu(menuId, request));
    }

    @DeleteMapping("/{menuId}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long menuId){
        menuService.deleteMenu(menuId);
        return ResponseEntity.noContent().build();
    }
}

package com.nbk.delivery_order.domain.menu.controller;

import com.nbk.delivery_order.domain.menu.dto.request.MenuCreateRequestDto;
import com.nbk.delivery_order.domain.menu.dto.response.MenuResponse;
import com.nbk.delivery_order.domain.menu.service.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;

    @PostMapping
    public ResponseEntity<MenuResponse> addMenu(@Valid @RequestBody MenuCreateRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(menuService.addMenu(request));
    }
}

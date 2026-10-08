package com.nbk.delivery_order.domain.menu.dto.response;

import com.nbk.delivery_order.domain.menu.entity.Menu;

public record MenuResponse(
       Long id,
       String name,
       Long price,
       String description,
       Long ownerId
) {
    public static MenuResponse from(Menu menu){
        return new MenuResponse(
                menu.getId(),
                menu.getName(),
                menu.getPrice(),
                menu.getDescription(),
                menu.getOwner().getId()
        );
    }
}

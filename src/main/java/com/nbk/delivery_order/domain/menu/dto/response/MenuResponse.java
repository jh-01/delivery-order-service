package com.nbk.delivery_order.domain.menu.dto.response;

import com.nbk.delivery_order.domain.menu.entity.Menu;
import com.nbk.delivery_order.domain.user.entity.User;

public record MenuResponse(
       Long id,
       User owner,
       Long price,
       String description
) {
    public MenuResponse from(Menu menu){
        return new MenuResponse(
                id, owner, price, description
        );
    }
}
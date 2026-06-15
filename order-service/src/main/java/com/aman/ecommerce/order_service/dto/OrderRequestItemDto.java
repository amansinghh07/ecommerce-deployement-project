package com.aman.ecommerce.order_service.dto;

import com.aman.ecommerce.order_service.entity.OrderStatus;
import lombok.Data;

@Data
public class OrderRequestItemDto {
    private Long id;
    private Long productId;
    private Integer quantity;
}

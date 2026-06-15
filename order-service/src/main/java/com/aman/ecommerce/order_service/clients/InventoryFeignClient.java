package com.aman.ecommerce.order_service.clients;

import com.aman.ecommerce.order_service.dto.OrderRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "inventory-service",path="/api/v1/inventory",url="${INVENTORY_SERVICE_URI:}")
public interface InventoryFeignClient {
    @PutMapping("/products/reduceStocks")
    public Double reduceStocks(@RequestBody OrderRequestDto orderRequestDto);
}

package com.aman.ecommerce.inventory_service.controller;

import com.aman.ecommerce.inventory_service.clients.OrderFeignClient;
import com.aman.ecommerce.inventory_service.dto.OrderRequestDto;
import com.aman.ecommerce.inventory_service.dto.ProductDto;
import com.aman.ecommerce.inventory_service.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final ProductService productService;
    private final DiscoveryClient discoveryClient;
    private final RestClient restClient;
    private final OrderFeignClient orderFeignClient;
    @GetMapping("/fetchOrders")
    public String fetchFromOrdersService(HttpServletRequest httpServletRequest){
        log.info("Fetching Orders from Order Service");
        log.info(httpServletRequest.getHeader("x-custom-header"));
//        ServiceInstance orderServiceInstance=discoveryClient.getInstances("order-service").getFirst();
/*        String response = restClient.get().uri(orderServiceInstance.getUri() + "/api/v1/orders/core/helloOrders ")
                .retrieve().body(String.class);
        log.info("response={}",response);
        return response;*/
        return orderFeignClient.helloOrders();
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> findAll(){
        List<ProductDto>inventories=productService.getAllInventory();
        return ResponseEntity.ok(inventories);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> findById(@PathVariable Long id){
      ProductDto inventory=productService.getProductById(id);
      return ResponseEntity.ok(inventory);
    }
    @PutMapping("/reduceStocks")
    public ResponseEntity<Double> reduceStocks(@RequestBody OrderRequestDto orderRequestDto){
        log.info("API HIT - ELK TEST LOG");
       Double totalPrice=productService.reduceStock(orderRequestDto);
       return ResponseEntity.ok(totalPrice);
    }
}

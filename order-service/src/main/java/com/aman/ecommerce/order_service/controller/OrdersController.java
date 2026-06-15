package com.aman.ecommerce.order_service.controller;

import com.aman.ecommerce.order_service.clients.InventoryFeignClient;
import com.aman.ecommerce.order_service.config.FeaturesEnableConfig;
import com.aman.ecommerce.order_service.dto.OrderRequestDto;
import com.aman.ecommerce.order_service.repository.OrderRepository;
import com.aman.ecommerce.order_service.service.OrderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/core")
@Slf4j
@RefreshScope
public class OrdersController {
    private final OrderService orderService;
    private final FeaturesEnableConfig featuresEnableConfig;
    @Value("${my.variable}")
    private String myVariable;
    @GetMapping("/helloOrders")
    public String helloOrders(){
        log.info("Hello from Orders Service");
        if(featuresEnableConfig.isUserTrackingEnabled()){
            return "User tracking enabled wohoo , my variable is :"+myVariable;
        } else{
            return "User tracking disabled awww , my variable is :"+myVariable;
        }

    }
    @GetMapping
    public ResponseEntity<List<OrderRequestDto>> getAllOrders(HttpServletRequest httpServletRequest){
    log.info("Fetching all orders");
    List<OrderRequestDto>orders=orderService.getAllOrders();
    return ResponseEntity.ok().body(orders);
    }
    @GetMapping("/{id}")
    public ResponseEntity<OrderRequestDto> getOrder(@PathVariable Long id){
        log.info("Fetching order with id {}", id);
        OrderRequestDto order=orderService.getOrderById(id);
        return ResponseEntity.ok().body(order);
    }
    @PostMapping("/create-orders")
    public ResponseEntity<OrderRequestDto> createOrder(@RequestBody OrderRequestDto orderRequestDto){
        log.info("Creating order with id {}", orderRequestDto.getId());
        OrderRequestDto orderRequestDto1=orderService.createOrder(orderRequestDto);
        return ResponseEntity.ok().body(orderRequestDto1);
    }
}

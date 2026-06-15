package com.aman.ecommerce.inventory_service.service;

import com.aman.ecommerce.inventory_service.dto.OrderRequestDto;
import com.aman.ecommerce.inventory_service.dto.OrderRequestItemDto;
import com.aman.ecommerce.inventory_service.dto.ProductDto;
import com.aman.ecommerce.inventory_service.entity.Product;
import com.aman.ecommerce.inventory_service.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public List<ProductDto> getAllInventory(){
        log.info("Getting all inventory");
        List<Product>inventories=productRepository.findAll();
        return inventories.stream().map
                (inventory -> modelMapper.map(inventory, ProductDto.class))
                .toList();
    }
    public ProductDto getProductById(Long id){
        log.info("Getting product by id");
        Optional<Product> inventory=productRepository.findById(id);
        return inventory.map(item->modelMapper.map(item,ProductDto.class)
        ).orElseThrow(()->new RuntimeException("Product not found with id: "+id));
    }
    @Transactional
    public Double reduceStock(OrderRequestDto orderRequestDto){
        log.info("Reducing the stock");
        Double totalPrice=0.0;
        for(OrderRequestItemDto item:orderRequestDto.getItems()){
            Long productId=item.getProductId();
            Integer quantity=item.getQuantity();
            Product product=productRepository.findById(productId).orElseThrow(
                    ()->new RuntimeException("Product not found with id: "+productId));
            if(product.getStock()<quantity){
                throw new RuntimeException("Product cannot be fulfilled for given quantity");
            }
            product.setStock(product.getStock()-quantity);
            productRepository.save(product);
            totalPrice+=quantity*product.getPrice();
        }
        return totalPrice;
    }

}

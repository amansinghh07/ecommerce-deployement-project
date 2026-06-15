package com.example.aman.api_gateway.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LoggingOrderFilter extends AbstractGatewayFilterFactory <LoggingOrderFilter.Config>{
    public LoggingOrderFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(LoggingOrderFilter.Config config) {
        return ((exchange, chain) ->{
            log.info("Order Filter Pre: {}",exchange.getRequest().getURI());
            return chain.filter(exchange);
        });
    }

    public static class Config {
    }
}

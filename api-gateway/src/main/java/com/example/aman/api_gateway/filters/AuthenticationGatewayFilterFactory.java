package com.example.aman.api_gateway.filters;

import com.example.aman.api_gateway.service.JwtService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthenticationGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthenticationGatewayFilterFactory.Config> {
    private final JwtService jwtService;
    public AuthenticationGatewayFilterFactory(JwtService jwtService) {
        super(Config.class);
        this.jwtService = jwtService;
    }

    @Override
    public GatewayFilter apply(Config config) {
        if(!config.isEnabled){
            return (exchange, chain) -> chain.filter(exchange);
        }
        return (exchange, chain) -> {;
            String headers=exchange.getRequest().getHeaders().getFirst("Authorization");
            if(headers==null || !headers.startsWith("Bearer ")){
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
            String token=headers.split("Bearer ")[1];
            Long userId = jwtService.getUserIdFromToken(token);
            exchange.getRequest()
                    .mutate()
                    .header("X-User-Id", userId.toString())
                    .build();
            return chain.filter(exchange);
        };
    }

    @Data
    public static class Config{
        private boolean isEnabled;
    }
}

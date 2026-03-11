package com.example.order.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.example.order.domain.dto.ProductResponseDTO;

/*
k8s에서 이 서비스를 사용하고 싶다면 url이 필요(http://product-service)
@FeignClient(name = "product-service")

*/
@FeignClient(name = "product-service", url = "http://product-service")

public interface ProductOpenFeignService {
    
    @GetMapping("/product/{productId}")
    public ProductResponseDTO getProductId(@PathVariable("productId") Long productId,
                                            @RequestHeader("X-User-Email") String email);
}

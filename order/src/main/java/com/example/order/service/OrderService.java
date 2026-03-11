package com.example.order.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.order.common.config.KafkaProducerConfig;
import com.example.order.dao.OrderRepository;
import com.example.order.domain.dto.OrderRequestDTO;
import com.example.order.domain.dto.OrderResponseDTO;
import com.example.order.domain.dto.ProductResponseDTO;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ProductOpenFeignService productOpenFeignService;
    private final OrderRepository orderRepository;
    private final KafkaProducerConfig kafkaProducerConfig;

    /*
    1. client(path: /order/create) -> 주문 요청
    2. orderFignKafkaCreate 실행
    3. feign 상품 조회(동기 방식)
    4. 재고 여부 확인
    5. kafka 토픽(kafkaTemplate.send())을 발행
    6. kafka broker(messageQ)에 메세지 저장
    7. product-service에서 수신(KafkaListener)하여  stockConsumer() 호출
    8. 재고 감소 구현(비동기 방식)
    */
   @CircuitBreaker(name = "productService", fallbackMethod = "fallbackProductService")
    public OrderResponseDTO orderFignKafkaCreate(OrderRequestDTO request, String email){
        System.out.println(">>>> order service orderFignKafkaCreate");
        System.out.println(">>>> 재고 유무 판단을 위한 product-service Feign 통신");

        ProductResponseDTO response = productOpenFeignService.getProductId(request.getProductId(), email);
        System.out.println(">>>> Feign 통신결과 : " + response.getStockQty());

        if(response.getStockQty() < request.getQty()){
            throw new RuntimeException("재고 부족");
        }else{
            System.out.println(">>>> order service 토픽 발행");
            kafkaProducerConfig.kafkaTemplate().send("update-stock-topic", request);
        }

        // 주문 저장
        return OrderResponseDTO.fromEntity(orderRepository.save(request.toEntity(email)));
    }

    public OrderResponseDTO fallbackProductService(OrderRequestDTO request, String email, Throwable t){


        throw new RuntimeException("서비스 지연으로 에러발생됨........ 다시 시도해주세요");
    }
}


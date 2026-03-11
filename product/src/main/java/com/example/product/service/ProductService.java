package com.example.product.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.product.dao.ProductRepository;
import com.example.product.domain.dto.ProductRequestDTO;
import com.example.product.domain.dto.ProductResponseDTO;
import com.example.product.domain.dto.ProductUpdateDTO;
import com.example.product.domain.entity.ProductEntity;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {
    
    private final ProductRepository productRepositiry;


    public ProductResponseDTO productCreate(ProductRequestDTO request, String email){
        System.out.println(">>>> product service productCreate");
        
        ProductEntity product = request.toEntity(email);
        return ProductResponseDTO.fromEntity(productRepositiry.save(product));

    }

    /*
    kafka - 비동기
    producer(order)
    - kafka(topic 발행 : update-stock-topic)
    - 그래서 @KafkaListener이 필요 그리고 String message 수신 - ObjectMapper(Json -> DTO)

    
    Consumer(product)
    - 메서드 호출 발생
    - 재고 업데이트
    */
    @KafkaListener(topics = "update-stock-topic")
    public void stockConsumer(String message) {
        ObjectMapper mapper = new ObjectMapper();
        ProductUpdateDTO productUpdateDTO = null;

        try {
            productUpdateDTO = mapper.readValue(message, ProductUpdateDTO.class);
            ProductEntity productEntity = productRepositiry
                                                        .findById(productUpdateDTO.getProductId())
                                                        .orElseThrow(() -> new RuntimeException(">>>> 상품이 존재하지 않습니다."));
            productEntity.updateStockQty(productUpdateDTO.getQty());
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }

    public ProductResponseDTO productRead(Long producyId){

        System.out.println(">>>> product service productRead : " + producyId);
        ProductEntity entity = productRepositiry.findById(producyId)
                            .orElseThrow(() -> new RuntimeException("상품 없음"));

        return ProductResponseDTO.fromEntity(entity);

    }
}

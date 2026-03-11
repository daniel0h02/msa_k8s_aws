package com.example.order.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="MSA_ORDER_TBL")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @Column(nullable = false)
    private String email ;

    @Column(nullable = false)
    private Long productId ;


    private Integer qty ; //수량
    
    @Enumerated(EnumType.STRING)
    @Builder.Default        //기본값을 세팅해줌, builder가 무시할 수 있어서(Emumerater) 그걸 방지라고 안정적이게 할 수 있음
    private OrderStatus orderStatus = OrderStatus.ORDERED ;

}

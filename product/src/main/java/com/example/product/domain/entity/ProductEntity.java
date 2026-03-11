package com.example.product.domain.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="MSA_PRODUCT_TBL")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    private String name ;
    private Integer price ;
    private Integer stockQty ;  //stock 관리

    //user가 상품을 등록하는 것 때문에 관계가 필요
    @Column(nullable = false)
    private String email ;

    //재고 관리를 위한 메서드 구현 *가장 핵심
    public void updateStockQty(int stockQty){
        this.stockQty = this.stockQty - stockQty ;
    }
}

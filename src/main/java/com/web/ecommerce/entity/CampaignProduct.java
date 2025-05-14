package com.web.ecommerce.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Index;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Table(
    name = "campaign_products",
    indexes = {
        @Index(name = "idx_product_id", columnList = "product_id"),
        @Index(name = "idx_campaign_id", columnList = "campaign_id")
    }
)
public class CampaignProduct extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "campaign_product_id")
    private int id;
    
    @Column(name = "campaign_id")
    private int campaignId;
    
    @Column(name = "product_id")
    private int productId;
    
    @Column(name = "discount_type", length = 20)
    private String discountType; // percentage or fixed
    
    @Column(name = "discount_value", precision = 10, scale = 2)
    private BigDecimal discountValue;
    
    @Column(name = "status")
    private int status;
} 
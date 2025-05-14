package com.web.ecommerce.response;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.web.ecommerce.entity.ProductImage;

import lombok.Data;

@Data
public class ProductImageResponse {
    private int id;
    
    @JsonProperty("product_id")
    private Integer productId;
    
    @JsonProperty("image_url")
    private String imageUrl;
    
    @JsonProperty("is_primary")
    private boolean isPrimary;
    
    private int status;
    
    public ProductImageResponse() {
        
    }
    
    public ProductImageResponse(ProductImage entity) {
        this.id = entity.getId();
        this.productId = entity.getProductId();
        this.imageUrl = entity.getImageUrl();
        this.isPrimary = entity.isPrimary();
        this.status = entity.getStatus();
    }
    
    public static List<ProductImageResponse> mapToList(List<ProductImage> entities) {
        return entities.stream().map(x -> new ProductImageResponse(x)).collect(Collectors.toList());
    }
}

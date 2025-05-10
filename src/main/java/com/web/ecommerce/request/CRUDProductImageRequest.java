package com.web.ecommerce.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CRUDProductImageRequest {
    @NotNull(message = "product_id không được phép để trống")
    @JsonProperty("product_id")
    private Integer productId;
    
    @NotEmpty(message = "image_url không được phép để trống")
    @Length(max = 255, message = "Đường dẫn hình ảnh không được phép lớn hơn 255 kí tự")
    @JsonProperty("image_url")
	private String imageUrl;
    
    @JsonProperty("is_primary")
    private boolean isPrimary = false;
}


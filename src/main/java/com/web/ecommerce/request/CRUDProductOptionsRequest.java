package com.web.ecommerce.request;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CRUDProductOptionsRequest {
    @NotNull(message = "product_id không được phép để trống")
    @JsonProperty("product_id")
	private Integer productId;
	
    @NotNull(message = "option_id không được phép để trống")
    @JsonProperty("option_id")
	private Integer optionId;
	
    @JsonProperty("option_value_id")
	private Integer optionValueId;
	
    @Min(value = 0, message = "Giá phải lớn hơn hoặc bằng 0")
    @JsonProperty("additional_price")
	private BigDecimal additionalPrice;
	
    @NotNull(message = "stock không được phép để trống")
    @Min(value = 0, message = "Stock phải lớn hơn hoặc bằng 0")
	private Integer stock;
}


package com.web.ecommerce.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.web.ecommerce.entity.ProductOptions;

import lombok.Data;

@Data
public class ProductOptionsResponse {
    private int id;
	
	@JsonProperty("product_id")
	private Integer productId;
	
	@JsonProperty("option_id")
	private Integer optionId;
	
	@JsonProperty("option_value_id")
	private Integer optionValueId;
	
	@JsonProperty("option_name")
	private String optionName;
	
	@JsonProperty("option_value_name")
	private String optionValueName;
	
	@JsonProperty("additional_price")
	private BigDecimal additionalPrice;
	
	private Integer stock;

	private int status;

	public ProductOptionsResponse() {

	}

	public ProductOptionsResponse(ProductOptions entity) {
		this.id = entity.getId();
		this.productId = entity.getProductId();
		this.optionId = entity.getOptionId();
		this.optionValueId = entity.getOptionValueId();
		this.additionalPrice = entity.getAdditionalPrice();
		this.stock = entity.getStock();
		this.status = entity.getStatus();
	}

	public static List<ProductOptionsResponse> mapToList(List<ProductOptions> entities) {
		return entities.stream().map(x -> new ProductOptionsResponse(x)).collect(Collectors.toList());
	}
}

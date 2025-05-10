package com.web.ecommerce.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.web.ecommerce.entity.Product;

import lombok.Data;

@Data
public class ProductResponse {
	private int id;
	private String name;
	
	private String description;
	
	private BigDecimal price;
	
	private int stock;
	
	@JsonProperty("is_parent")
	private boolean isParent;
	
	@JsonProperty("parent_id")
	private Integer parentId;
	
	@JsonProperty("category_id")
	private Integer categoryId;
	
	private String sku;

	private int status;

	public ProductResponse() {

	}

	public ProductResponse(Product entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.description = entity.getDescription();
		this.price = entity.getPrice();
		this.stock = entity.getStock();
		this.isParent = entity.isParent();
		this.parentId = entity.getParentId();
		this.categoryId = entity.getCategoryId();
		this.sku = entity.getSku();
		this.status = entity.getStatus();
	}

	public static List<ProductResponse> mapToList(List<Product> entities) {
		return entities.stream().map(x -> new ProductResponse(x)).collect(Collectors.toList());
	}
}

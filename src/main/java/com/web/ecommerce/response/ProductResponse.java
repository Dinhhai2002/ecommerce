package com.web.ecommerce.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.web.ecommerce.entity.Product;
import com.web.ecommerce.model.ProductFillterDto;

import lombok.Data;

@Data
public class ProductResponse {
	private int id;

	private String name;

	private String description;

	private BigDecimal price;

	private int stock;

	@JsonProperty("is_parent")
	private int isParent;

	@JsonProperty("parent_id")
	private Integer parentId;

	@JsonProperty("category_id")
	private Integer categoryId;

	private String sku;

	private int status;

	@JsonProperty("main_image_url")
	private String mainImageUrl;
	
	@JsonProperty("original_price")
	private BigDecimal orginalPrice;

	@JsonProperty("sale_price")
	private BigDecimal salePrice;

	@JsonProperty("discount_type")
	private int discountType;
	
	@JsonProperty("discount_value")
	private BigDecimal discountValue;	
	
	@JsonProperty("has_discount")
	private int hasDiscount;

	@JsonProperty("product_options")
	private List<ProductOptionsResponse> productOptions;
	
	@JsonProperty("product_images")
	private List<ProductImageResponse> productImages;

	public ProductResponse() {

	}

	public ProductResponse(Product entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.description = entity.getDescription();
		this.price = entity.getPrice();
		this.stock = entity.getStock();
		this.isParent = entity.getIsParent();
		this.parentId = entity.getParentId();
		this.categoryId = entity.getCategoryId();
		this.sku = entity.getSku();
		this.status = entity.getStatus();
	}

	public ProductResponse(Product entity, List<ProductOptionsResponse> productOptions, List<ProductImageResponse> productImages) {
		this(entity);
		this.productOptions = productOptions;
		this.productImages = productImages;
	}

	public ProductResponse(ProductFillterDto entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.description = entity.getDescription();
		this.price = entity.getPrice();
		this.stock = entity.getStock();
		this.isParent = entity.getIsParent();
		this.parentId = entity.getParentId();
		this.categoryId = entity.getCategoryId();
		this.sku = entity.getSku();
		this.status = entity.getStatus();
		this.mainImageUrl = entity.getMainImageUrl();
		this.orginalPrice = entity.getOrginalPrice();
		this.salePrice = entity.getSalePrice();
		this.discountType = entity.getDiscountType();
		this.discountValue = entity.getDiscountValue();
		this.hasDiscount = entity.getHasDiscount();
	}

	public static List<ProductResponse> mapToList(List<Product> entities) {
		return entities.stream().map(x -> new ProductResponse(x)).collect(Collectors.toList());
	}

	public static List<ProductResponse> mapToListDTO(List<ProductFillterDto> entities) {
		return entities.stream().map(x -> new ProductResponse(x)).collect(Collectors.toList());
	}
}

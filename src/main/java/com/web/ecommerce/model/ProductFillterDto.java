package com.web.ecommerce.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.web.ecommerce.entity.BaseEntity;

import lombok.Data;

@Data
@Entity
public class ProductFillterDto extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Id
	@Column(name="product_id")
	private int id;

	@Column(name = "name", nullable = false, length = 100)
	private String name;
	
	@Column(name = "description", columnDefinition = "text")
	private String description;
	
	@Column(name = "price", precision = 10, scale = 2)
	private BigDecimal price;
	
	@Column(name = "stock", nullable = false)
	private int stock;
	
	@Column(name = "is_parent", nullable = false)
	private int isParent;
	
	@Column(name = "parent_id")
	private Integer parentId;
	
	@Column(name = "category_id")
	private Integer categoryId;
	
	@Column(name = "sku", nullable = false, unique = true, length = 20)
	private String sku;
	
	@Column(name = "status")
	private int status;
	
	@Column(name = "main_image_url")
	private String mainImageUrl;
	
	@Column(name = "original_price", precision = 10, scale = 2)
	private BigDecimal orginalPrice;

	@Column(name = "sale_price", precision = 10, scale = 2)
	private BigDecimal salePrice;

	@Column(name = "discount_type")
	private int discountType;
	
	@Column(name = "discount_value")
	private BigDecimal discountValue;	
	
	@Column(name = "has_discount")
	private int hasDiscount;
	
}

package com.web.ecommerce.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Column;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Table(name = "products")
public class Product extends BaseEntity{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	private boolean isParent;
	
	@Column(name = "parent_id")
	private Integer parentId;
	
	@Column(name = "category_id")
	private Integer categoryId;
	
	@Column(name = "sku", nullable = false, unique = true, length = 20)
	private String sku;
	
	@Column(name = "status")
	private int status;
}

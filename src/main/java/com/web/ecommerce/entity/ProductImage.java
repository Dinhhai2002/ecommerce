package com.web.ecommerce.entity;

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
    name = "product_images",
    indexes = {
        @Index(name = "idx_product_id", columnList = "product_id")
    }
)
public class ProductImage extends BaseEntity{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "image_id")
	private int id;
	
	@Column(name = "product_id")
	private Integer productId;
	
	@Column(name = "image_url", nullable = false)
	private String imageUrl;
	
	@Column(name = "is_primary", columnDefinition = "tinyint(1) default 0")
	private boolean isPrimary;

	@Column(name = "status")
	private int status;
}

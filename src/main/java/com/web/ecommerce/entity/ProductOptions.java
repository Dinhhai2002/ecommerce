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
    name = "product_options", 
    indexes = {
        @Index(name = "idx_product_id", columnList = "product_id")
    }
)
public class ProductOptions extends BaseEntity{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_option_id")
	private int id;
	
	@Column(name = "product_id")
	private Integer productId;
	
	@Column(name = "option_id")
	private Integer optionId;
	
	@Column(name = "option_value_id")
	private Integer optionValueId;
	
	@Column(name = "additional_price", precision = 10, scale = 2)
	private BigDecimal additionalPrice;
	
	@Column(name = "stock")
	private Integer stock;

	@Column(name = "status")
	private int status;
}

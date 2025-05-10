package com.web.ecommerce.request;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CRUDProductRequest {
    @NotEmpty(message = "name không được phép để trống")
	@Length(max = 100, message = "Tên sản phẩm không được phép lớn hơn 100 kí tự")
	private String name;

	private String description;
	
	@NotNull(message = "price không được phép để trống")
	@Min(value = 0, message = "Giá phải lớn hơn hoặc bằng 0")
	private BigDecimal price;
	
	@NotNull(message = "stock không được phép để trống")
	@Min(value = 0, message = "Stock phải lớn hơn hoặc bằng 0")
	private Integer stock;
	
	@JsonProperty("is_parent")
	private boolean isParent;
	
	@JsonProperty("parent_id")
	private Integer parentId;
	
	@JsonProperty("category_id")
	private Integer categoryId;
	
	@NotEmpty(message = "sku không được phép để trống")
	@Length(max = 20, message = "SKU không được phép lớn hơn 20 kí tự")
	@Pattern(regexp = "^[A-Za-z0-9-_]+$", message = "SKU chỉ được chứa chữ cái, số, dấu gạch ngang và gạch dưới")
	private String sku;
}


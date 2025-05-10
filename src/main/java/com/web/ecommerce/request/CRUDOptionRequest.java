package com.web.ecommerce.request;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class CRUDOptionRequest {
   @NotEmpty(message = "name không được phép để trống")
	@Length(max = 50, message = "Tên không được phép lớn hơn 50 kí tự")
	private String name;

}


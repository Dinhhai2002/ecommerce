package com.web.ecommerce.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CRUDOptionValuesRequest {

	@Min(value = 1, message = "option_id phải lớn hơn 0")
	@Max(value = 1000000, message = "option_id phải nhỏ hơn 1000000")
	@JsonProperty("option_id")
	private Integer optionId;

	@NotEmpty(message = "value không được phép để trống")
	@Length(max = 255, message = "Không được phép lớn hơn 255 kí tự")
	@JsonProperty("value")
	private String value;

}


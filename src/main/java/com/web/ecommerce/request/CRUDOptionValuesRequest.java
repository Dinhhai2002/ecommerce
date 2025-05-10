package com.web.ecommerce.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CRUDOptionValuesRequest {
    @NotNull(message = "option_id không được phép để trống")
    @JsonProperty("option_id")
    private Integer optionId;
    
    @NotEmpty(message = "value không được phép để trống")
	@Length(max = 50, message = "Giá trị không được phép lớn hơn 50 kí tự")
	private String value;
}


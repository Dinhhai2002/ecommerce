package com.web.ecommerce.request;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data

public class CRUDCategoryRequest {
    @NotEmpty(message = "name không được phép để trống")
	@Length(max = 255, message = "Không được phép lớn hơn 255 kí tự")
	private String name;

    @JsonProperty("parent_id")
    private int parentId;

    @JsonProperty("image_url")
    private String imageUrl;

}


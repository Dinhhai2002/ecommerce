package com.web.ecommerce.request;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ApiPermissionRequest {

	@JsonProperty("api_path")
    private String apiPath;

    @JsonProperty("http_method")
    private String httpMethod;

    @JsonProperty("role_id")
    private Integer roleId;

    private String description;
    
	
}

package com.web.ecommerce.response;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.web.ecommerce.entity.OptionValues;

import lombok.Data;

@Data
public class OptionValuesResponse {
    @JsonProperty("option_value_id")
    private int id;
    
    @JsonProperty("option_id")
    private Integer optionId;

	private String value;

	private int status;

	public OptionValuesResponse() {

	}

	public OptionValuesResponse(OptionValues entity) {
		this.id = entity.getId();
		this.optionId = entity.getOptionId();
		this.value = entity.getValue();
		this.status = entity.getStatus();
	}

	public static List<OptionValuesResponse> mapToList(List<OptionValues> entities) {
		return entities.stream().map(x -> new OptionValuesResponse(x)).collect(Collectors.toList());
	}
}

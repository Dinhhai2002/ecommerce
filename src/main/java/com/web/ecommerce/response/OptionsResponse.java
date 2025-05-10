package com.web.ecommerce.response;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.web.ecommerce.entity.Options;

import lombok.Data;

@Data
public class OptionsResponse {
    @JsonProperty("option_id")
    private int id;

	private String name;

	private int status;

	public OptionsResponse() {

	}

	public OptionsResponse(Options entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.status = entity.getStatus();
	}

	public static List<OptionsResponse> mapToList(List<Options> entities) {
		return entities.stream().map(x -> new OptionsResponse(x)).collect(Collectors.toList());
	}
}

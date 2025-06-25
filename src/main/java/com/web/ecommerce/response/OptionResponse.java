package com.web.ecommerce.response;

import java.util.List;
import java.util.stream.Collectors;

import com.web.ecommerce.entity.Option;

import lombok.Data;

@Data
public class OptionResponse {
    private int id;

	private String name;

	private int status;

	public OptionResponse() {

	}

	public OptionResponse(Option entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.status = entity.getStatus();
	}

	public List<OptionResponse> mapToList(List<Option> entities) {
		return entities.stream().map(x -> new OptionResponse(x)).collect(Collectors.toList());
	}
}

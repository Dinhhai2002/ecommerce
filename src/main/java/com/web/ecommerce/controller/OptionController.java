package com.web.ecommerce.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.web.ecommerce.common.utils.Pagination;
import com.web.ecommerce.common.utils.StringErrorValue;
import com.web.ecommerce.entity.Option;
import com.web.ecommerce.model.StoreProcedureListResult;
import com.web.ecommerce.request.CRUDOptionRequest;
import com.web.ecommerce.response.BaseListDataResponse;
import com.web.ecommerce.response.BaseResponse;
import com.web.ecommerce.response.OptionResponse;
import com.web.ecommerce.service.OptionService;

@RestController
@RequestMapping("/api/v1/option")
public class OptionController  {
    @Autowired
    public OptionService optionService;

    @GetMapping("")
//	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<BaseResponse<BaseListDataResponse<OptionResponse>>> getAll(
			@RequestParam(name = "key_search", required = false, defaultValue = "") String keySearch,
			@RequestParam(name = "status", required = false, defaultValue = "-1") int status,
			@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			@RequestParam(name = "limit", required = false, defaultValue = "10") int limit) throws Exception {
		BaseResponse<BaseListDataResponse<OptionResponse>> response = new BaseResponse<>();
		Pagination pagination = new Pagination(page, limit);
		StoreProcedureListResult<Option> listOption = optionService.spGListOption(keySearch,
				status, pagination);

		BaseListDataResponse<OptionResponse> listData = new BaseListDataResponse<>();

		listData.setList(new OptionResponse().mapToList(listOption.getResult()));
		listData.setTotalRecord(listOption.getTotalRecord());

		response.setData(listData);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
    
    @GetMapping("/{id}")
	public ResponseEntity<BaseResponse<OptionResponse>> findOneById(@PathVariable("id") int id) throws Exception {
		BaseResponse<OptionResponse> response = new BaseResponse<>();
		Option option = optionService.findOne(id);

		if (option == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.OPTION_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
        response.setData(new OptionResponse(option));

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/{id}/change-status")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<BaseResponse<OptionResponse>> changeStatus(@PathVariable("id") int id) throws Exception {
		BaseResponse<OptionResponse> response = new BaseResponse<>();
		Option option = optionService.findOne(id);

		if (option == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.OPTION_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		option.setStatus(option.getStatus() == 1 ? 0 : 1);

		optionService.update(option);
        response.setData(new OptionResponse(option));

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/create")
	public ResponseEntity<BaseResponse<OptionResponse>> create(
			@Valid @RequestBody CRUDOptionRequest wrapper) throws Exception {

		BaseResponse<OptionResponse> response = new BaseResponse<>();
		Option optionCheck = optionService.findByName(wrapper.getName());

		if (optionCheck != null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.OPTION_IS_EXIST);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		Option option = new Option();
		option.setName(wrapper.getName());
		option.setStatus(1);

		optionService.create(option);
		response.setData(new OptionResponse(option));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/{id}/update")
	public ResponseEntity<BaseResponse<OptionResponse>> update(@PathVariable("id") int id,
			@Valid @RequestBody CRUDOptionRequest wrapper) throws Exception {

		BaseResponse<OptionResponse> response = new BaseResponse<>();
		Option option = optionService.findOne(id);

		if (option == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.OPTION_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		if (!option.getName().equals(wrapper.getName())
				&& optionService.findByName(wrapper.getName()) != null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.OPTION_IS_EXIST);
			return new ResponseEntity<>(response, HttpStatus.OK);

		}
		option.setName(wrapper.getName());
		optionService.update(option);

		response.setData(new OptionResponse(option));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}

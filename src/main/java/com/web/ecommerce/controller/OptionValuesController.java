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
import com.web.ecommerce.entity.OptionValues;
import com.web.ecommerce.model.StoreProcedureListResult;
import com.web.ecommerce.request.CRUDOptionValuesRequest;
import com.web.ecommerce.response.BaseListDataResponse;
import com.web.ecommerce.response.BaseResponse;
import com.web.ecommerce.response.OptionValuesResponse;
import com.web.ecommerce.service.OptionService;
import com.web.ecommerce.service.OptionValuesService;

@RestController
@RequestMapping("/api/v1/option-values")
public class OptionValuesController  {
    @Autowired
    public OptionValuesService optionValuesService;

	@Autowired
	public OptionService optionService;

    @GetMapping("")
//	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<BaseResponse<BaseListDataResponse<OptionValuesResponse>>> getAll(
			@RequestParam(name = "key_search", required = false, defaultValue = "") String keySearch,
			@RequestParam(name = "status", required = false, defaultValue = "-1") int status,
			@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			@RequestParam(name = "limit", required = false, defaultValue = "10") int limit) throws Exception {
		BaseResponse<BaseListDataResponse<OptionValuesResponse>> response = new BaseResponse<>();
		Pagination pagination = new Pagination(page, limit);
		StoreProcedureListResult<OptionValues> listOptionValues = optionValuesService.spGListOptionValues(keySearch,
				status, pagination);

		BaseListDataResponse<OptionValuesResponse> listData = new BaseListDataResponse<>();

		listData.setList(new OptionValuesResponse().mapToList(listOptionValues.getResult()));
		listData.setTotalRecord(listOptionValues.getTotalRecord());

		response.setData(listData);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
    
    @GetMapping("/{id}")
	public ResponseEntity<BaseResponse<OptionValuesResponse>> findOneById(@PathVariable("id") int id) throws Exception {
		BaseResponse<OptionValuesResponse> response = new BaseResponse<>();
		OptionValues optionValues = optionValuesService.findOne(id);

		if (optionValues == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.OPTION_VALUES_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
        response.setData(new OptionValuesResponse(optionValues));

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/{id}/change-status")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<BaseResponse<OptionValuesResponse>> changeStatus(@PathVariable("id") int id) throws Exception {
		BaseResponse<OptionValuesResponse> response = new BaseResponse<>();
		OptionValues optionValues = optionValuesService.findOne(id);

		if (optionValues == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.OPTION_VALUES_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		optionValues.setStatus(optionValues.getStatus() == 1 ? 0 : 1);

		optionValuesService.update(optionValues);
        response.setData(new OptionValuesResponse(optionValues));

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/create")
	public ResponseEntity<BaseResponse<OptionValuesResponse>> create(
			@Valid @RequestBody CRUDOptionValuesRequest wrapper) throws Exception {

		BaseResponse<OptionValuesResponse> response = new BaseResponse<>();
		OptionValues optionValuesCheck = optionValuesService.findByName(wrapper.getValue());

		if (optionValuesCheck != null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.OPTION_VALUES_IS_EXIST);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		Option option = optionService.findOne(wrapper.getOptionId());
		if (option == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.OPTION_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		OptionValues optionValues = new OptionValues();
		optionValues.setOptionId(wrapper.getOptionId());
		optionValues.setValue(wrapper.getValue());
		optionValues.setStatus(1);

		optionValuesService.create(optionValues);
		response.setData(new OptionValuesResponse(optionValues));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/{id}/update")
	public ResponseEntity<BaseResponse<OptionValuesResponse>> update(@PathVariable("id") int id,
			@Valid @RequestBody CRUDOptionValuesRequest wrapper) throws Exception {

		BaseResponse<OptionValuesResponse> response = new BaseResponse<>();
		OptionValues optionValues = optionValuesService.findOne(id);

		if (optionValues == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.OPTION_VALUES_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		optionValues.setValue(wrapper.getValue());
		optionValuesService.update(optionValues);

		response.setData(new OptionValuesResponse(optionValues));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}

package com.web.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.ecommerce.common.utils.StringErrorValue;
import com.web.ecommerce.entity.Option;
import com.web.ecommerce.response.BaseResponse;
import com.web.ecommerce.response.OptionResponse;
import com.web.ecommerce.service.OptionService;

@RestController
@RequestMapping("/api/v1/options")
public class OptionController extends BaseController<Option, OptionResponse, Integer> {
    public OptionController() {
        super(OptionResponse::new);
    }
    
    @Autowired
    OptionService optionService;
    
    @PostMapping("/{id}/change-status")
    public ResponseEntity<BaseResponse<OptionResponse>> changeStatus(@PathVariable("id") int id) throws Exception {
        BaseResponse<OptionResponse> response = new BaseResponse<>();

        Option optionEntity = optionService.findOne(id);
        if (optionEntity == null) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessageError(StringErrorValue.OPTION_NOT_FOUND);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        optionEntity.setStatus(optionEntity.getStatus() == 1 ? 0 : 1);
        optionService.update(optionEntity);

        response.setData(new OptionResponse(optionEntity));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

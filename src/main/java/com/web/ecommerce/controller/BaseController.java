package com.web.ecommerce.controller;

import com.web.ecommerce.common.utils.Pagination;
import com.web.ecommerce.model.StoreProcedureListResult;
import com.web.ecommerce.response.BaseResponse;
import com.web.ecommerce.service.BaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class BaseController<T, R, ID extends Serializable> {

    @Autowired
    protected BaseService<T, ID> baseService;

    // Function mapping entity -> response DTO
    private final Function<T, R> mapper;

    protected BaseController(Function<T, R> mapper) {
        this.mapper = mapper;
    }

    protected <D> ResponseEntity<BaseResponse<D>> buildResponse(D data, HttpStatus status, String messageError) {
        BaseResponse<D> response = new BaseResponse<>();
        response.setData(data);
        response.setStatus(status);
        response.setMessageError(messageError);
        return new ResponseEntity<>(response, status);
    }

    @GetMapping
    @Operation(summary = "Get all entities", description = "Lấy danh sách tất cả entities")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Danh sách trả về thành công",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class))),
        @ApiResponse(responseCode = "500", description = "Lỗi server")
    })
    public ResponseEntity<BaseResponse<List<R>>> getAll() {
        List<R> data = baseService.getAll().stream().map(mapper).collect(Collectors.toList());
        return buildResponse(data, HttpStatus.OK, "success");
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get entity by ID", description = "Lấy thông tin entity theo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Thông tin trả về thành công",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class))),
        @ApiResponse(responseCode = "404", description = "Không tìm thấy entity"),
        @ApiResponse(responseCode = "500", description = "Lỗi server")
    })
    public ResponseEntity<BaseResponse<R>> getById(@Parameter(description = "ID của entity") @PathVariable ID id) {
        T entity = baseService.findOne(id);
        if (entity == null) {
            return buildResponse(null, HttpStatus.NOT_FOUND, "Not found");
        }
        return buildResponse(mapper.apply(entity), HttpStatus.OK, "success");
    }

    @PostMapping
    @Operation(summary = "Create entity", description = "Tạo mới entity")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tạo mới thành công",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class))),
        @ApiResponse(responseCode = "500", description = "Lỗi server")
    })
    public ResponseEntity<BaseResponse<R>> create(@Parameter(description = "Entity cần tạo") @RequestBody T entity) {
        baseService.create(entity);
        return buildResponse(mapper.apply(entity), HttpStatus.OK, "create success");
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update entity", description = "Cập nhật entity theo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cập nhật thành công",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class))),
        @ApiResponse(responseCode = "500", description = "Lỗi server")
    })
    public ResponseEntity<BaseResponse<R>> update(
            @Parameter(description = "ID của entity") @PathVariable ID id,
            @Parameter(description = "Entity cập nhật") @RequestBody T entity) {
        baseService.update(entity);
        return buildResponse(mapper.apply(entity), HttpStatus.OK, "update success");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete entity", description = "Xóa entity theo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Xóa thành công",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class))),
        @ApiResponse(responseCode = "404", description = "Không tìm thấy entity"),
        @ApiResponse(responseCode = "500", description = "Lỗi server")
    })
    public ResponseEntity<BaseResponse<Void>> delete(@Parameter(description = "ID của entity") @PathVariable ID id) {
        T entity = baseService.findOne(id);
        if (entity == null) {
            return buildResponse(null, HttpStatus.NOT_FOUND, "Not found");
        }
        baseService.delete(entity);
        return buildResponse(null, HttpStatus.OK, "deleted success");
    }

    @GetMapping("/search")
    @Operation(summary = "Tìm kiếm entities", description = "Tìm kiếm entities theo điều kiện")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Danh sách trả về thành công",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BaseResponse.class))),
        @ApiResponse(responseCode = "500", description = "Lỗi server")
    })
    public ResponseEntity<BaseResponse<List<R>>> getListWithCondition(
            @RequestParam(required = false) String keySearch,
            @RequestParam(defaultValue = "-1") int status,
            Pagination pagination
    ) throws Exception {
        StoreProcedureListResult<T> data = baseService.getListWithCondition(keySearch, status, pagination);
        List<R> mapped = data.getResult().stream().map(mapper).collect(Collectors.toList());
        return buildResponse(mapped, HttpStatus.OK, null);
    }
}

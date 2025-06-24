package com.web.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.ecommerce.service.EndpointService;
import com.web.ecommerce.service.EndpointService.ApiEndpointInfo;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/system")
@Slf4j
public class SystemController extends BaseUtilsController {

    @Autowired
    private EndpointService endpointService;

    /**
     * Lấy danh sách tất cả API endpoints
     */
    @GetMapping("/endpoints")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getAllEndpoints() {
        try {
            List<ApiEndpointInfo> endpoints = endpointService.getAllEndpoints();
            return buildResponse(endpoints, HttpStatus.OK, "Lấy danh sách endpoints thành công");
        } catch (Exception e) {
            log.error("Lỗi khi lấy danh sách endpoints: {}", e.getMessage(), e);
            return buildResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi khi lấy danh sách endpoints");
        }
    }

    /**
     * Refresh danh sách endpoints
     */
    @GetMapping("/endpoints/refresh")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> refreshEndpoints() {
        try {
            endpointService.refreshEndpoints();
            List<ApiEndpointInfo> endpoints = endpointService.getAllEndpoints();
            return buildResponse(endpoints, HttpStatus.OK, "Refresh danh sách endpoints thành công");
        } catch (Exception e) {
            log.error("Lỗi khi refresh endpoints: {}", e.getMessage(), e);
            return buildResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi khi refresh endpoints");
        }
    }
} 
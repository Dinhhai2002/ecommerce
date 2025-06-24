package com.web.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.ecommerce.entity.ApiPermission;
import com.web.ecommerce.request.ApiPermissionRequest;
import com.web.ecommerce.service.ApiPermissionService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/permissions")
@Slf4j
public class ApiPermissionController extends BaseUtilsController {

    @Autowired
    private ApiPermissionService apiPermissionService;

    /**
     * Lấy danh sách tất cả permissions
     */
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getAllPermissions() {
        try {
            List<ApiPermission> permissions = apiPermissionService.getAll();
            return buildResponse(permissions, HttpStatus.OK, "Lấy danh sách permissions thành công");
        } catch (Exception e) {
            log.error("Lỗi khi lấy danh sách permissions: {}", e.getMessage(), e);
            return buildResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi khi lấy danh sách permissions");
        }
    }

    /**
     * Lấy permission theo ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getPermissionById(@PathVariable Integer id) {
        try {
            ApiPermission permission = apiPermissionService.findOne(id);
            if (permission == null) {
                return buildResponse(null, HttpStatus.NOT_FOUND, "Không tìm thấy permission với ID: " + id);
            }
            return buildResponse(permission, HttpStatus.OK, "Lấy thông tin permission thành công");
        } catch (Exception e) {
            log.error("Lỗi khi lấy permission theo ID {}: {}", id, e.getMessage(), e);
            return buildResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi khi lấy thông tin permission");
        }
    }

    /**
     * Lấy danh sách permissions theo role ID
     */
    @GetMapping("/role/{roleId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getPermissionsByRoleId(@PathVariable Integer roleId) {
        try {
            List<ApiPermission> permissions = apiPermissionService.getPermissionsByRoleId(roleId);
            return buildResponse(permissions, HttpStatus.OK, "Lấy danh sách permissions theo role thành công");
        } catch (Exception e) {
            log.error("Lỗi khi lấy permissions theo role ID {}: {}", roleId, e.getMessage(), e);
            return buildResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi khi lấy danh sách permissions");
        }
    }

    /**
     * Tạo permission mới
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> createPermission(@RequestBody ApiPermissionRequest permission) {
        try {
        	ApiPermission apiPermissionEntity = new ApiPermission();
        	apiPermissionEntity.setApiPath(permission.getApiPath());
        	apiPermissionEntity.setHttpMethod(permission.getHttpMethod());
        	apiPermissionEntity.setRoleId(permission.getRoleId());
        	apiPermissionEntity.setDescription(permission.getDescription());
            apiPermissionService.create(apiPermissionEntity);
            return buildResponse("success", HttpStatus.CREATED, "Tạo permission thành công");
        } catch (Exception e) {
            log.error("Lỗi khi tạo permission: {}", e.getMessage(), e);
            return buildResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi khi tạo permission");
        }
    }

    /**
     * Cập nhật permission
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updatePermission(@PathVariable Integer id, @RequestBody ApiPermission permission) {
        try {
            ApiPermission existingPermission = apiPermissionService.findOne(id);
            if (existingPermission == null) {
                return buildResponse(null, HttpStatus.NOT_FOUND, "Không tìm thấy permission với ID: " + id);
            }

            permission.setId(id);
            apiPermissionService.update(permission);
            return buildResponse("success", HttpStatus.OK, "Cập nhật permission thành công");
        } catch (Exception e) {
            log.error("Lỗi khi cập nhật permission với ID {}: {}", id, e.getMessage(), e);
            return buildResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi khi cập nhật permission");
        }
    }

    /**
     * Xóa permission
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deletePermission(@PathVariable Integer id) {
        try {
            ApiPermission existingPermission = apiPermissionService.findOne(id);
            if (existingPermission == null) {
                return buildResponse(null, HttpStatus.NOT_FOUND, "Không tìm thấy permission với ID: " + id);
            }

            apiPermissionService.delete(existingPermission);
            return buildResponse(null, HttpStatus.OK, "Xóa permission thành công");
        } catch (Exception e) {
            log.error("Lỗi khi xóa permission với ID {}: {}", id, e.getMessage(), e);
            return buildResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi khi xóa permission");
        }
    }
} 
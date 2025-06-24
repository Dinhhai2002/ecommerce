package com.web.ecommerce.common.aspect;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.web.ecommerce.common.annotation.CheckPermission;
import com.web.ecommerce.entity.Users;
import com.web.ecommerce.response.BaseResponse;
import com.web.ecommerce.service.ApiPermissionService;
import com.web.ecommerce.service.UserService;
import com.web.ecommerce.dao.UserRoleDao;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class PermissionAspect {

    @Autowired
    private ApiPermissionService apiPermissionService;

    @Autowired
    private UserService userService;
    
    @Autowired
    private UserRoleDao userRoleDao;

    @Around("@annotation(checkPermission)")
    public Object checkPermission(ProceedingJoinPoint joinPoint, CheckPermission checkPermission) throws Throwable {
        
        try {
            // Lấy thông tin request hiện tại
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes == null) {
                log.warn("Không thể lấy thông tin request");
                return joinPoint.proceed();
            }

            HttpServletRequest request = attributes.getRequest();
            String apiPath = request.getRequestURI();
            String httpMethod = request.getMethod();

            // Lấy thông tin user hiện tại
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                log.warn("User chưa được xác thực");
                return buildResponse(null, HttpStatus.UNAUTHORIZED, "User chưa được xác thực");
            }

            String username = authentication.getName();
            Users user = userService.findUsersByUsersName(username);
            
            if (user == null) {
                log.warn("Không tìm thấy user: {}", username);
                return buildResponse(null, HttpStatus.UNAUTHORIZED, "Không tìm thấy thông tin user");
            }

            // Lấy danh sách role_id của user
            List<Integer> roleIds = userRoleDao.findRoleIdsByUserId(user.getId());
            
            if (roleIds.isEmpty()) {
                log.warn("User {} không có role nào", username);
                return buildResponse(null, HttpStatus.FORBIDDEN, "User không có quyền truy cập");
            }

            // Kiểm tra quyền truy cập cho từng role
            boolean hasPermission = false;
            for (Integer roleId : roleIds) {
                if (apiPermissionService.hasPermission(apiPath, httpMethod, roleId)) {
                    hasPermission = true;
                    break;
                }
            }
            
            if (!hasPermission) {
                log.warn("User {} không có quyền truy cập API: {} {}", username, httpMethod, apiPath);
                return buildResponse(null, HttpStatus.FORBIDDEN, "Không có quyền truy cập API này");
            }

            log.info("User {} có quyền truy cập API: {} {}", username, httpMethod, apiPath);
            
            // Nếu có quyền, thực hiện method gốc
            return joinPoint.proceed();

        } catch (Exception e) {
            log.error("Lỗi khi kiểm tra quyền: {}", e.getMessage(), e);
            return buildResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi khi kiểm tra quyền");
        }
    }

    /**
	 * Build response với format chuẩn
	 */
	protected <D> ResponseEntity<BaseResponse<D>> buildResponse(D data, HttpStatus status, String messageError) {
		BaseResponse<D> response = new BaseResponse<>();
		response.setData(data);
		response.setStatus(status);
		response.setMessageError(messageError);
		return new ResponseEntity<>(response, status);
	}
} 
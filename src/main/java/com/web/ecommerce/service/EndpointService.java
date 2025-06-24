package com.web.ecommerce.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EndpointService {

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    private List<ApiEndpointInfo> availableEndpoints = new ArrayList<>();

    /**
     * DTO để lưu thông tin endpoint
     */
    public static class ApiEndpointInfo {
        private String path;
        private String method;

        public ApiEndpointInfo(String path, String method) {
            this.path = path;
            this.method = method;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        @Override
        public String toString() {
            return "ApiEndpointInfo{" +
                    "path='" + path + '\'' +
                    ", method='" + method + '\'' +
                    '}';
        }
    }

    /**
     * Quét tất cả endpoints khi service được khởi tạo
     */
    @PostConstruct
    public void discoverEndpoints() {
        try {
            availableEndpoints.clear();
            
            // Lấy tất cả mappings
            Set<RequestMappingInfo> mappings = requestMappingHandlerMapping.getHandlerMethods().keySet();
            
            for (RequestMappingInfo mapping : mappings) {
                if (mapping.getPatternsCondition() != null) {
                    Set<String> patterns = mapping.getPatternsCondition().getPatterns();
                    Set<RequestMethod> methods = mapping.getMethodsCondition().getMethods();
                    
                    for (String pattern : patterns) {
                        if (methods.isEmpty()) {
                            // Nếu không có method cụ thể, thêm tất cả methods
                            availableEndpoints.add(new ApiEndpointInfo(pattern, "GET"));
                            availableEndpoints.add(new ApiEndpointInfo(pattern, "POST"));
                            availableEndpoints.add(new ApiEndpointInfo(pattern, "PUT"));
                            availableEndpoints.add(new ApiEndpointInfo(pattern, "DELETE"));
                        } else {
                            for (RequestMethod method : methods) {
                                availableEndpoints.add(new ApiEndpointInfo(pattern, method.name()));
                            }
                        }
                    }
                }
            }
            
            log.info("Đã quét được {} endpoints", availableEndpoints.size());
            
        } catch (Exception e) {
            log.error("Lỗi khi quét endpoints: {}", e.getMessage(), e);
        }
    }

    /**
     * Lấy danh sách tất cả endpoints
     */
    public List<ApiEndpointInfo> getAllEndpoints() {
        return new ArrayList<>(availableEndpoints);
    }

    /**
     * Lấy danh sách endpoints theo method
     */
    public List<ApiEndpointInfo> getEndpointsByMethod(String method) {
        return availableEndpoints.stream()
                .filter(endpoint -> endpoint.getMethod().equalsIgnoreCase(method))
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Lấy danh sách endpoints theo path pattern
     */
    public List<ApiEndpointInfo> getEndpointsByPathPattern(String pathPattern) {
        return availableEndpoints.stream()
                .filter(endpoint -> endpoint.getPath().contains(pathPattern))
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Refresh danh sách endpoints
     */
    public void refreshEndpoints() {
        discoverEndpoints();
    }
} 
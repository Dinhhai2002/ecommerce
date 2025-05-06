package com.web.ecommerce.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.ecommerce.response.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        BaseResponse<Object> baseResponse = new BaseResponse<>();
        baseResponse.setStatus(HttpStatus.FORBIDDEN);
        baseResponse.setMessageError("Bạn không có quyền truy cập tài nguyên này!");
        baseResponse.setData(null);

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(baseResponse));
    }
}

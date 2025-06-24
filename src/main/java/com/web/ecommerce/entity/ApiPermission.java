package com.web.ecommerce.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Table(name = "api_permissions")
public class ApiPermission extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "api_path", nullable = false)
    private String apiPath;

    @Column(name = "http_method", nullable = false)
    private String httpMethod;

    @Column(name = "role_id", nullable = false)
    private Integer roleId;

    private String description;
    
    public ApiPermission() {}
    
    public ApiPermission(String apiPath, String httpMethod, Integer roleId, String description) {
        this.apiPath = apiPath;
        this.httpMethod = httpMethod;
        this.roleId = roleId;
        this.description = description;
    }
} 
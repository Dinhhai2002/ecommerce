package com.web.ecommerce.entity;

import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "roles")
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    // Constructors
    public Role() {}
    public Role(String name) {
        this.name = name;
    }
}

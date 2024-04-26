package com.lagrandee.kinMel.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "roles")
@Setter
@Getter
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", nullable = false)
    private Integer id;

    @Column(name = "name",  length = 50)
    private String name;

    @Column(name = "description", length = 250)
    private String description;

    public Role() {
    }
    public Role(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
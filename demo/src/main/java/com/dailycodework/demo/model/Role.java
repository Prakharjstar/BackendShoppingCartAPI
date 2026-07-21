package com.dailycodework.demo.model;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.HashSet;

@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role(String name) {
        this.name = name;
    }

    @ManyToMany(mappedBy = "roles" )
    private Collection<User> users = new HashSet<>();
}

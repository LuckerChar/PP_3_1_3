package ru.kata.spring.boot_security.demo.models;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Data
@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column (name ="name")
    private String name;

    @Override
    public String toString() {
        return name.replace("ROLE_", "");
    }

    @Override
    public String getAuthority() {
        return getName();
    }
}

package com.clothshop.account.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_user")
public class User {

    @Id
    @Column(name = "_id")
    private String id;

    @Column(name = "_name")
    private String name;

    @Column(name = "_email")
    private String email;

    @Column(name = "_password")
    private String password;

    @Column(name = "_enabled")
    private boolean isEnabled;

    @ManyToMany
    @JoinTable(name = "tbl_user_role", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns =
            {@JoinColumn(name = "role_id")})
    private Set<Role> roles = new HashSet<>();
}

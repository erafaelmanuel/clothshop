package com.clothshop.accountservice.data.entity;

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
@Table(name = "tbl_role")
public class Role {

    @Id
    @Column(name = "_id")
    private String id;

    @Column(name = "_name")
    private String name;

    @ManyToMany(mappedBy = "roles", cascade = CascadeType.REMOVE)
    private Set<User> users = new HashSet<>();
}

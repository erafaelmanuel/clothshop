package com.clothshop.catalog.data.entity;

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
@Table(name = "tbl_category")
public class Category {

    @Id
    @Column(name = "_id")
    private String id;

    @Column(name = "_name")
    private String name;

    @Column(name = "_description")
    private String description;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    private Category parent;

    @ManyToMany(mappedBy = "categories")
    private Set<Item> items = new HashSet<>();
}

package com.clothshop.catalogservice.rest.dto;

import com.rem.mappyfy.Bind;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Relation(value = "item", collectionRelation = "items")
public class ItemDto extends ResourceSupport {

    @Bind(fields = {"id"})
    private String uid;

    private String name;

    private String description;

    private Double price;

    private String imageUrl;
}

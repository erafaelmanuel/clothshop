package com.clothshop.catalogservice.rest.dto;

import com.rem.mappyfy.Bind;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.ResourceSupport;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto extends ResourceSupport {

    @Bind(fields = {"id"})
    private String uid;

    private String name;

    private String description;
}

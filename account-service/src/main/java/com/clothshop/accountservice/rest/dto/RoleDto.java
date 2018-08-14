package com.clothshop.accountservice.rest.dto;

import com.rem.mappyfy.Bind;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.ResourceSupport;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto extends ResourceSupport {

    @Bind(fields = {"id"})
    private String uid;
    private String name;
}

package com.clothshop.accountservice.rest.dto;

import com.rem.mappyfy.Bind;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.ResourceSupport;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserDto extends ResourceSupport {

    @Bind(fields = {"id"})
    private String uid;
    private String name;
    private String email;
    private String password;
    private boolean isEnabled;
}

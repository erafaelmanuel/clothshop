package com.clothshop.client.web.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {

    @JsonAlias("uid")
    private String id;
    private String name;
    private String email;
    private String password;
    private boolean isEnabled;
}

package com.crudops.skylark.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String message;

    @Getter
    private Long userId;

}

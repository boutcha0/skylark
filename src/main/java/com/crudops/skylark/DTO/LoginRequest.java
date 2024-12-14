// src/main/java/com/crudops/skylark/dto/LoginRequest.java

package com.crudops.skylark.DTO;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;

}

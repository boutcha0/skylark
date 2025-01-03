package com.crudops.skylark.DTO;

import com.crudops.skylark.model.Info;
import lombok.Data;

@Data
public class InfosDTO extends Info {

    private Long id;
    private String email;
    private String password;
    private String adresse;
    private String name;
}


package com.crudops.skylark.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter

public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String image;
}

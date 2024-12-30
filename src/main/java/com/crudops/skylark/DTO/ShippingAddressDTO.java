package com.crudops.skylark.DTO;

import lombok.Data;

@Data
public class ShippingAddressDTO {
    private Long id;
    private String streetAddress;
    private String city;
    private String state;
    private String postalCode;
    private String country;
}
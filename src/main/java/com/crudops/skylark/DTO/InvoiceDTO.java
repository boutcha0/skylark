package com.crudops.skylark.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InvoiceDTO {

    private Long id;
    private LocalDate invoiceDate;
    private double totalAmount;

    public InvoiceDTO(String s) {
    }
}

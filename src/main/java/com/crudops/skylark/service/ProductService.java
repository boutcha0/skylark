package com.crudops.skylark.service;

import com.crudops.skylark.DTO.ProductDTO;
import java.util.List;

public interface ProductService {
    List<ProductDTO> getAllProducts();
    ProductDTO getProductById(Long id);
}

package com.crudops.skylark.service;

import com.crudops.skylark.DTO.ProductDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductService {
    List<ProductDTO> getAllProducts();

    ProductDTO getProductById(Long id);

    @Transactional
    ProductDTO createProduct(ProductDTO productDTO);

    @Transactional
    ProductDTO updateProduct(Long id, ProductDTO productDTO);

    @Transactional
    void deleteProduct(Long id);
}

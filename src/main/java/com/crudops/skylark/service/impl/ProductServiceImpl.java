package com.crudops.skylark.service.impl;

import com.crudops.skylark.DTO.ProductDTO;
import com.crudops.skylark.mapper.ProductMapper;
import com.crudops.skylark.model.Product;
import com.crudops.skylark.repository.ProductRepository;
import com.crudops.skylark.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDTO getProductById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
    }

    @Override
    @Transactional
    public ProductDTO createProduct(ProductDTO productDTO) {
        // Ensure no ID is set for a new product
        productDTO.setId(null);
        Product product = productMapper.toEntity(productDTO);
        Product savedProduct = productRepository.save(product);
        return productMapper.toDTO(savedProduct);
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        // Find existing product
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));

        // MapDTO to entity, preserving the existing ID
        Product updatedProduct = productMapper.toEntity(productDTO);
        updatedProduct.setId(existingProduct.getId());

        // Save the updated product
        Product savedProduct = productRepository.save(updatedProduct);
        return productMapper.toDTO(savedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        // Check if product exists before deleting
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));

        productRepository.delete(product);
    }
}
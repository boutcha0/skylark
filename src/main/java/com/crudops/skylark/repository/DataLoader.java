package com.crudops.skylark.repository;

import com.crudops.skylark.model.Product;
import com.crudops.skylark.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    public CommandLineRunner loadData(ProductRepository productRepository) {
        return args -> {
            if (productRepository.count() == 0) {
                productRepository.save(new Product(1L, "Product A", "Red", 19.99, "https://example.com/images/product-a.jpg"));
                productRepository.save(new Product(2L, "Product B", "Blue", 29.99, "https://example.com/images/product-b.jpg"));
                productRepository.save(new Product(3L, "Product C", "Green", 39.99, "https://example.com/images/product-c.jpg"));
            }
        };
    }
}

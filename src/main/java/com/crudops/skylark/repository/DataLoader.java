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
                productRepository.save(new Product(1L, "Product A", "Red", 70.0, "https://cdn.shopify.com/s/files/1/0054/3391/7538/products/workershirt.jpg?v=1619726495"));
                productRepository.save(new Product(2L, "Product B", "Blue", 60.0, "https://th.bing.com/th/id/OIP.kn-87u89NlcbvmsTmSEdxgHaJ3?w=1420&h=1892&rs=1&pid=ImgDetMain"));
                productRepository.save(new Product(3L, "Product C", "Green", 90.0, "https://www.nativeskatestore.co.uk/images/city-lights-pullover-hood-chocolate-p62349-141609_image.jpg"));
                productRepository.save(new Product(3L, "Product D", "Multi", 100.0, "https://sevenues.de/cdn/shop/products/dick.jpg?v=1677265682&width=1946"));
            }
        };
    }
}

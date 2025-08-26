package com.enterprise.product.product_service.domain.repository;

import com.enterprise.product.product_service.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByPublicId(UUID publicId);
    Optional<Product> findByName(String name);

    boolean existsByName(String name);
}

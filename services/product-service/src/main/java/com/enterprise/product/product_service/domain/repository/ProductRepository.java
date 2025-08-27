package com.enterprise.product.product_service.domain.repository;

import com.enterprise.product.product_service.domain.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Optional<Product> findByPublicId(UUID publicId);
    Optional<Product> findByName(String name);

    boolean existsByName(String name);
    Page<Product> findByNameContainingIgnoreCase(String search, Pageable pageable);
}

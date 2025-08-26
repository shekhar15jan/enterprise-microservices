package com.enterprise.product.product_service.domain.service.impl;

import com.enterprise.product.product_service.domain.entity.Product;
import com.enterprise.product.product_service.domain.repository.ProductRepository;
import com.enterprise.product.product_service.domain.service.ProductMapper;
import com.enterprise.product.product_service.domain.service.ProductService;
import com.enterprise.product.product_service.exceptions.ProductAlreadyExistsException;
import com.enterprise.product.product_service.exceptions.ProductServiceException;
import com.enterprise.product.product_service.web.dto.ProductDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Objects.requireNonNull(productDto, "productDto cannot be null");
        log.info("Creating product {}", productDto);
        if(productRepository.existsByName(productDto.name())){
            throw new ProductAlreadyExistsException("Product with name "+productDto.name()+" is already exist");
        }
        try {
            Product product = ProductMapper.toProduct(productDto);
            return ProductMapper.toProductDto(productRepository.save(product));
        } catch (DataIntegrityViolationException e) {
            log.info("Failed to create product {} ", productDto.name(), e);
            throw new ProductServiceException("Database Error while creating product "+productDto.name()+"-"+e);
        }
    }
}

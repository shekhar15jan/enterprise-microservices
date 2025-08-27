package com.enterprise.product.product_service.domain.service.impl;

import com.enterprise.product.product_service.domain.entity.Product;
import com.enterprise.product.product_service.domain.repository.ProductRepository;
import com.enterprise.product.product_service.domain.service.ProductMapper;
import com.enterprise.product.product_service.domain.service.ProductService;
import com.enterprise.product.product_service.domain.specification.ProductSearchCriteria;
import com.enterprise.product.product_service.domain.specification.ProductSpecification;
import com.enterprise.product.product_service.domain.specification.SearchParser;
import com.enterprise.product.product_service.exceptions.ProductAlreadyExistsException;
import com.enterprise.product.product_service.exceptions.ProductNotFoundException;
import com.enterprise.product.product_service.exceptions.ProductServiceException;
import com.enterprise.product.product_service.web.dto.ProductDto;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

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
            log.error("Failed to create product {} ", productDto.name(), e);
            throw new ProductServiceException("Database Error while creating product "+productDto.name(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "products", key="#publicId")
    public ProductDto getByPublicId(UUID publicId) {
        Objects.requireNonNull(publicId, "publicId cannot be null");
        log.info("Getting product by public id {}", publicId);
        try {
            Product product = productRepository.findByPublicId(publicId).orElseThrow(() -> new ProductNotFoundException(publicId));
            log.debug("Product {} found for  public id {}", product, publicId);
            return ProductMapper.toProductDto(product);
        }catch (DataIntegrityViolationException e) {
            log.error("Failed to get product by public id {} ", publicId, e);
            throw new ProductServiceException("Database Error while getting product "+publicId, e);
        }
    }

    @Override
    public Page<ProductDto> getAllProducts(Pageable pageable, String search) {
        log.info("traceId={} - Fetching all products page={}, size={}, sort={}, search={}",
                MDC.get("traceId"),
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort(),
                search);
        ProductSearchCriteria searchCriteria = SearchParser.parse(search);
        Specification<Product> spec = (search == null || search.isEmpty()) ? null : ProductSpecification.searchInField(searchCriteria);
        Page<Product> productPage = (spec == null) ? productRepository.findAll(pageable) : productRepository.findAll(spec, pageable);
//
//        if(search == null || search.isEmpty()){
//            productPage = productRepository.findAll(pageable);
//        }else {
//            spec = ProductSpecification.searchInField(search);
//            productPage =  productRepository.findAll(spec, pageable);
//        }
        return productPage.map(ProductMapper::toProductDto);
    }

    @Override
    @Transactional
    @CachePut(value = "products", key="#publicId")
    public ProductDto updateProduct(UUID publicId, ProductDto productDto) {
        Objects.requireNonNull(publicId, "publicId cannot be null");
        Objects.requireNonNull(productDto, "productDto cannot be null");
        try {
            Product existingProduct = productRepository.findByPublicId(publicId).orElseThrow(() -> new ProductNotFoundException(publicId));
            existingProduct.setName(productDto.name());
            existingProduct.setDescription(productDto.description());
            existingProduct.setPrice(productDto.price());
            existingProduct.setStockQuantity(productDto.stockQuantity());
            Product saveProduct = productRepository.save(existingProduct);
            log.info("Product {} updated for  updated product {}", saveProduct, publicId);
            return ProductMapper.toProductDto(saveProduct);
        }catch (DataIntegrityViolationException e) {
            log.error("Failed to update product {} ", productDto.name(), e);
            throw new ProductServiceException("Database Error while updating product "+productDto.name(), e);
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = "products", key = "#publicId")
    public void deleteProduct(UUID publicId) {
        Objects.requireNonNull(publicId, "publicId cannot be null");
        log.info("Deleting product by public id {}", publicId);
        try {
            Product product = productRepository.findByPublicId(publicId).orElseThrow(() -> new ProductNotFoundException(publicId));
            productRepository.delete(product);
            log.info("Deleted product by public id {}", publicId);
        }catch (DataIntegrityViolationException e) {
            log.error("Failed to delete product {} ", publicId, e);
            throw new ProductServiceException("Database Error while deleting product "+publicId,e);
        }
    }

}

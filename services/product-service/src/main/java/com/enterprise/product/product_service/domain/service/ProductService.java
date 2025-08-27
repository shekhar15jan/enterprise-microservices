package com.enterprise.product.product_service.domain.service;

import com.enterprise.product.product_service.web.dto.ProductDto;
import com.enterprise.product.product_service.web.dto.ProductRequest;
import com.enterprise.product.product_service.web.dto.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductService {

    ProductDto createProduct(ProductDto productDto);
    ProductDto updateProduct(UUID publicId,ProductDto productDto);
    ProductDto getByPublicId(UUID publicId);
    Page<ProductDto> getAllProducts(Pageable pageable, String search);
    void deleteProduct(UUID publicId);
//
//    ProductDto increseStock(UUID publicId, int quantity);
//
//    ProductDto decreaseStock(UUID publicId,int quantity);
}

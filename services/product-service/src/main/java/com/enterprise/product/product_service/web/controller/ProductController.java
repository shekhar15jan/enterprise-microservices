package com.enterprise.product.product_service.web.controller;

import com.enterprise.product.product_service.domain.service.ProductService;
import com.enterprise.product.product_service.web.dto.ProductDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api${api.version.v1}${api.resource.product}") //endpoint: /api/v1/products
//@RequestMapping("/api/v1/products")
@Slf4j
public class ProductController {
    private final ProductService productService;
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    //POST: Create Product
    @Operation(summary = "Create Product", description = "Adds a new product")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "409", description = "Product already exists")
    })
    @PostMapping
    public ResponseEntity<ProductDto> addProduct(@RequestBody @Valid ProductDto productRequest) {
        log.info("trace={} Create new product with name ={}", MDC.get("traceId"), productRequest.name());
        ProductDto createdProduct = productService.createProduct(productRequest);
        return ResponseEntity.created(URI.create("/api/v1/products/"+createdProduct.publicId())).body(createdProduct);
    }
    //GET: Get Product by Id
    @Operation(summary = "Get Product by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getAllProducts(@PathVariable UUID id) {
        ProductDto foundProduct = productService.getByPublicId(id);
        return ResponseEntity.ok(foundProduct);
    }

    //Get: All product
    @Operation(summary = "Get All Products with Pagination and searching")
    @GetMapping
    public ResponseEntity<Page<ProductDto>> getAllProducts(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(value = "search", required = false) String search
            ) {
        log.info("traceId={} - Fetching all products with pagination page={}, size={}, sort={}",
                MDC.get("traceId"), pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        Page<ProductDto> page = productService.getAllProducts(pageable, search);
        return ResponseEntity.ok(page);
    }
    //Put: Update Product
    @Operation(summary = "Update product by Id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product updated"),
            @ApiResponse(responseCode = "404", description = "Products does not exists")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable UUID id, @RequestBody @Valid ProductDto productRequest) {
        ProductDto updatedProduct = productService.updateProduct(id, productRequest);
        return ResponseEntity.ok(updatedProduct);
    }
    //Delete: Delete Product
    @Operation(summary = "Delete product by Id")
    @DeleteMapping("/{id}")
    public ResponseEntity<ProductDto> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}

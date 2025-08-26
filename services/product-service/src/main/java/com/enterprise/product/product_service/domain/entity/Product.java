package com.enterprise.product.product_service.domain.entity;

import com.enterprise.product.product_service.domain.entity.audit.AuditableEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(
        name="product",
        indexes={
                @Index(name="idx_product_name", columnList = "name")
        },
        uniqueConstraints={
                @UniqueConstraint(name="uk_product_name", columnNames = {"name"})
        }
)
@Getter
@Setter
public class Product extends AuditableEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false, columnDefinition = "BINARY(16)")
    private UUID publicId = UUID.randomUUID(); //external id

    @Column(nullable = false, length = 150)
    @NotBlank(message = "Product name is required")
    private String name;
    @Column(length = 1000)
    private String description;

    @Column(nullable = false, precision = 19, scale = 2)
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "price must be greater than 0")
    private BigDecimal price;
    @Column(nullable = false)
    @Min(value = 0, message = "Stock must be >=0")
    private Integer stockQuantity;

    @Getter
    @Version
    private Long version; //optimistic locking

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return publicId.equals(product.publicId) && name.equals(product.name);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public Product() { }
    public Product(Long id, UUID publicId, String name, String description, BigDecimal price, Integer stockQuantity, Long version) {
        this.id = id;
        this.publicId = publicId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.version = version;
    }
    public static Builder builder(){
        return new Builder();
    }
    public static class Builder {
        private Long id;
        private UUID publicId;
        private String name;
        private String description;
        private BigDecimal price;
        private Integer stockQuantity;
        private Long version;

        public Builder id(Long id) {this.id = id; return this;}
        public Builder publicId(UUID publicId) {this.publicId = publicId; return this;}
        public Builder name(String name) {this.name = name; return this;}
        public Builder description(String description) {this.description = description; return this;}
        public Builder price(BigDecimal price) {this.price = price; return this;}
        public Builder stockQuantity(Integer stockQuantity) {this.stockQuantity = stockQuantity; return this;}
        public Builder version(Long version) {this.version = version; return this;}
        public Product build() {
            Product product = new Product();
            product.publicId = publicId;
            product.name = name;
            product.description = description;
            product.price = price;
            product.stockQuantity = stockQuantity;
            product.version = version;
            return product;
        }
    }

}

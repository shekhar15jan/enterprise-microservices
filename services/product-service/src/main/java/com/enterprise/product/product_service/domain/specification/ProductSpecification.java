package com.enterprise.product.product_service.domain.specification;

import com.enterprise.product.product_service.domain.entity.Product;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

    public static Specification<Product> searchInField(ProductSearchCriteria productSearchCriteria){
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(productSearchCriteria.getText() != null && !productSearchCriteria.getText().isEmpty()) {
                String likeSearch = "%" + productSearchCriteria.getText().toLowerCase() + "%";
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likeSearch),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), likeSearch)
                ));
            }
            if(productSearchCriteria.getMinPrice() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), productSearchCriteria.getMinPrice()));
            }
            if(productSearchCriteria.getMaxPrice() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"),productSearchCriteria.getMaxPrice()));
            }
            if(productSearchCriteria.getMinStock() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("stockQuantity"),productSearchCriteria.getMinStock()));
            }
            if(productSearchCriteria.getMinStock() != null) {
             predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("stockQuantity"),productSearchCriteria.getMaxStock()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

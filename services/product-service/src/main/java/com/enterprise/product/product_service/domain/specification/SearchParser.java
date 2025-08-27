package com.enterprise.product.product_service.domain.specification;

import java.math.BigDecimal;

public class SearchParser {
    public static ProductSearchCriteria parse(String search) {
        ProductSearchCriteria criteria = new ProductSearchCriteria();
        if (search == null || search.isEmpty()) return criteria;

        String[] keyValues = search.split("&");
        for (String keyValue : keyValues) {
            String[] pair = keyValue.split("=");
            if (pair.length != 2) continue;

            String key = pair[0].trim();
            String value = pair[1].trim();

            switch (key) {
                case "name":
                    criteria.setText(value);
                    break;
                case "minPrice":
                    criteria.setMinPrice(new BigDecimal(value));
                    break;
                case "maxPrice":
                    criteria.setMaxPrice(new BigDecimal(value));
                    break;
                case "minStock":
                    criteria.setMinStock(Integer.parseInt(value));
                    break;
                case "maxStock":
                    criteria.setMaxStock(Integer.parseInt(value));
                    break;
            }
        }
        return criteria;
    }
}


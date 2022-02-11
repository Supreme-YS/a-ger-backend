package com.ireland.ager.product.dto.response;

import com.ireland.ager.product.entity.Category;
import com.ireland.ager.product.entity.Product;
import com.ireland.ager.product.entity.ProductStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class ProductThumbResponse {
    String productName;
    String productPrice;
    Long productViewCnt;
    Category category;
    LocalDateTime createdAt;
    String thumbNailUrl;

    public static ProductThumbResponse toProductThumbResponse(Product product) {
        return ProductThumbResponse.builder()
                .productName(product.getProductName())
                .productPrice(product.getProductPrice())
                .productViewCnt(product.getProductViewCnt())
                .category(product.getCategory())
                .createdAt(product.getCreatedAt())
                .thumbNailUrl(product.getThumbNailUrl())
                .build();
    }

    public static List<ProductThumbResponse> toProductListResponse(List<Product> productList) {
        List<ProductThumbResponse> productResponseList = new ArrayList<>();
        for (Product product : productList) {
            ProductThumbResponse productResponse = ProductThumbResponse.toProductThumbResponse(product);
            productResponseList.add(productResponse);
        }
        return productResponseList;
    }
}

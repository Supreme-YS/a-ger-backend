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
    Long productId;
    String productName;
    String productPrice;
    String productDetail;
    Long productViewCnt;
    Category category;
    ProductStatus status;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    String thumbNailUrl;

    public static ProductThumbResponse toProductThumbResponse(Product product) {
        return ProductThumbResponse.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .productPrice(product.getProductPrice())
                .productDetail(product.getProductDetail())
                .productViewCnt(product.getProductViewCnt())
                .category(product.getCategory())
                .status(product.getStatus())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
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

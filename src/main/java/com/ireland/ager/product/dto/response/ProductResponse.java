package com.ireland.ager.product.dto.response;

import com.ireland.ager.product.entity.Category;
import com.ireland.ager.product.entity.Product;
import com.ireland.ager.product.entity.ProductStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
public class ProductResponse {
    Long productId;
    String productName;
    String productPrice;
    String productDetail;
    Long productViewCnt;
    Category category;
    ProductStatus status;
    private List<String> urlList = new ArrayList<>();

    public static ProductResponse toProductResponse(Product product) {
        return ProductResponse.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .productPrice(product.getProductPrice())
                .productDetail(product.getProductDetail())
                .productViewCnt(product.getProductViewCnt())
                .category(product.getCategory())
                .status(product.getStatus())
                .urlList(product.getUrlList())
                .build();
    }
}
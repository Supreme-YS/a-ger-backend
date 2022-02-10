package com.ireland.ager.product.dto.request;

import com.ireland.ager.product.entity.Product;
import com.ireland.ager.product.entity.ProductStatus;
import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class StatusUpdateRequest {
    private Long productId;
    private ProductStatus status;

    public Product toProductStatusUpdate(Long productId, ProductStatus productStatus) {
        return Product.builder()
                .productId(productId)
                .status(productStatus)
                .build();
    }
}

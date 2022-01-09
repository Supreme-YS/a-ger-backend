package com.ireland.ager.product.dto.request;

import lombok.Data;

@Data
public class ProductRequest {
    String productName;
    String productPrice;
    String productDetail;

    String categoryName;

    // 이미지 관련한 것도 추가해야 할듯?

}

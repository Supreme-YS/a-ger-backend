package com.ireland.ager.product.dto.request;

import com.ireland.ager.account.entity.Account;
import com.ireland.ager.product.entity.Category;
import com.ireland.ager.product.entity.Product;
import com.ireland.ager.product.entity.ProductStatus;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Data
public class ProductUpdateRequest {
    String productName;
    String productPrice;
    String productDetail;
    String category;
    String status;


    public Product toProductUpdate(Product product, Account account,
                                   List<String> uploadImageUrl) {
        List<String> images = new ArrayList<>();
        for (String url : uploadImageUrl) {
            images.add(url);
        }
        product.setUrlList(images);
        product.setProductDetail(this.productDetail);
        product.setProductPrice(productPrice);
        product.setProductViewCnt(product.getProductViewCnt());
        product.setProductName(productName);
        product.setStatus(ProductStatus.valueOf(this.status));
        product.setCategory(Category.valueOf(this.category));
        return product;
    }

//    public Product toProductStatusUpdateDone(Product productStatus) {
//        productStatus.setStatus(Status.판매완료);
//        return productStatus;
//    }
//
//    public Product toProductStatusUpdateIng(Product productStatus) {
//        productStatus.setStatus(Status.판매중);
//    }
//
//    public Product toProductStatusUpdateDone(Product productStatus) {
//        productStatus.setStatus(Status.판매완료);
//    }
}
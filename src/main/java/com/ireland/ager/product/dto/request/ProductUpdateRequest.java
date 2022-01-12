package com.ireland.ager.product.dto.request;

import com.ireland.ager.account.entity.Account;
import com.ireland.ager.product.entity.Category;
import com.ireland.ager.product.entity.Product;
import com.ireland.ager.product.entity.Status;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Data
public class ProductUpdateRequest {
    String productName;
    String productPrice;
    String productDetail;
    String category;
    String status;


    public Product toProductUpdate(Product product,Account account,
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
        product.setStatus(Status.valueOf(this.status));
        product.setCategory(Category.valueOf(this.category));
        return product;
    }
}
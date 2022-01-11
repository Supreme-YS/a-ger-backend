package com.ireland.ager.product.dto.request;

import com.ireland.ager.account.entity.Account;
import com.ireland.ager.product.entity.Category;
import com.ireland.ager.product.entity.Product;
import com.ireland.ager.product.entity.Status;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class ProductRequest {
    String productName;
    String productPrice;
    String productDetail;
    String category;

    public Product toProduct(Optional<Account> account,

                             List<String> uploadImageUrl) {
        Product product=new Product();
        List<String> images=new ArrayList<>();
        for(String url: uploadImageUrl) {
            images.add(url);
        }
        product.addAccount(account);
        product.setUrlList(images);
        product.setProductDetail(this.productDetail);
        product.setProductPrice(productPrice);
        product.setProductViewCnt(0L);
        product.setProductName(productName);
        product.setStatus(Status.판매중);
        product.setCategory(Category.valueOf(this.category));
        return product;
    }
}

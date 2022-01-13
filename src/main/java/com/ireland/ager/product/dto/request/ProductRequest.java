package com.ireland.ager.product.dto.request;

import com.ireland.ager.account.entity.Account;
import com.ireland.ager.product.entity.Category;
import com.ireland.ager.product.entity.Product;
import com.ireland.ager.product.entity.Status;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductRequest {
    String productName;
    String productPrice;
    String productDetail;
    String category;


    public Product toProduct(Account account,
        List<String> uploadImageUrl) {
        Product product=new Product();
        List<String> images=new ArrayList<>();
        for(String url: uploadImageUrl) {
            images.add(url);
        }
        //FIXME Optional Account 반환 문제 해결해야한다.
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
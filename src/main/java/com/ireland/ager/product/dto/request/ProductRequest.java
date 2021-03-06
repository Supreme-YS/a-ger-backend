package com.ireland.ager.product.dto.request;

import com.ireland.ager.account.entity.Account;
import com.ireland.ager.product.entity.Category;
import com.ireland.ager.product.entity.Product;
import com.ireland.ager.product.entity.ProductStatus;
import com.ireland.ager.product.entity.Url;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProductRequest {
    @NotBlank(message = "3010")
    String productName;
    @NotBlank(message = "3020")
    @Min(value = 0, message = "3021")
    String productPrice;
    @NotBlank(message = "3030")
    String productDetail;
    @NotBlank(message = "3040")
    String category;

    public Product toProduct(Account account,
                             List<String> uploadImageUrl, String thumbNailUrl) {
        Product product = new Product();
        for (String str : uploadImageUrl) {
            Url url = new Url();
            url.setUrl(str);
            product.addUrl(url);
        }
        product.addAccount(account);
        product.setProductDetail(this.productDetail);
        product.setProductPrice(productPrice);
        product.setProductViewCnt(0L);
        product.setProductName(productName);
        product.setStatus(ProductStatus.SALE);
        product.setThumbNailUrl(thumbNailUrl);
        product.setCategory(Category.valueOf(this.category));
        return product;
    }
}
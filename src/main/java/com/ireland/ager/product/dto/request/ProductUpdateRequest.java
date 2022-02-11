package com.ireland.ager.product.dto.request;

import com.ireland.ager.account.entity.Account;
import com.ireland.ager.product.entity.Category;
import com.ireland.ager.product.entity.Product;
import com.ireland.ager.product.entity.ProductStatus;
import com.ireland.ager.product.entity.Url;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Data
public class ProductUpdateRequest {
    @NotBlank(message = "3010")
    String productName;
    @NotBlank(message = "3020")
    @Min(value = 0, message = "3021")
    String productPrice;
    @NotBlank(message = "3030")
    String productDetail;
    @NotBlank(message = "3040")
    String category;
    @NotBlank(message = "3050")
    String status;

    public Product toProductUpdate(Product product, Account account,
                                   List<String> uploadImageUrl) {
        for (String str : uploadImageUrl) {
            Url url = new Url();
            url.setUrl(str);
            product.addUrl(url);
        }
        product.setProductDetail(this.productDetail);
        product.setProductPrice(productPrice);
        product.setProductViewCnt(product.getProductViewCnt());
        product.setProductName(productName);
        product.setStatus(ProductStatus.valueOf(this.status));
        product.setCategory(Category.valueOf(this.category));
        return product;
    }
}
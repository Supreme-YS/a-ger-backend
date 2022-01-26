package com.ireland.ager.product.dto.request;

import com.ireland.ager.account.entity.Account;
import com.ireland.ager.product.entity.Category;
import com.ireland.ager.product.entity.Product;
import com.ireland.ager.product.entity.ProductStatus;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProductRequest {
    @NotBlank(message = "상품 제목은 필수 항목입니다.")
    String productName;
    @Min(value = 0,message="상품 가격을 확인해주세요")
    String productPrice;
    @NotBlank(message = "상품 상세정보는 필수 항목입니다.")
    String productDetail;
    String category;

    public Product toProduct(Account account,
                             List<String> uploadImageUrl) {
        Product product = new Product();
        List<String> images = new ArrayList<>();
        for (String url : uploadImageUrl) {
            images.add(url);
        }
        //FIXME Optional Account 반환 문제 해결해야한다.
        product.addAccount(account);
        product.setUrlList(images);
        product.setProductDetail(this.productDetail);
        product.setProductPrice(productPrice);
        product.setProductViewCnt(0L);
        product.setProductName(productName);
        product.setStatus(ProductStatus.SALE);
        product.setCategory(Category.valueOf(this.category));
        return product;
    }
}
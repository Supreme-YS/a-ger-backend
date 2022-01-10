package com.ireland.ager.product.dto.request;

import com.ireland.ager.product.entity.Category;
import com.ireland.ager.product.entity.Photo;
import com.ireland.ager.product.entity.Product;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class ProductRequest {
    String productName;
    String productPrice;
    String productDetail;
    String category;

    public Product toProduct(List<String> uploadImageUrl) {
        Product product=new Product();
        List<Photo> photos=new ArrayList<>();
        for(String url: uploadImageUrl) {
            Photo photo=new Photo(url);
            photos.add(photo);
        }
        product.setPhotoUrlList(photos);
        product.setProductDetail(this.productDetail);
        product.setProductPrice(productPrice);
        product.setProductViewCnt(0L);
        product.setProductName(productName);
        product.setCategory(Category.valueOf(this.category));
        return product;
    }
}

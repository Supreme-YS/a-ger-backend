//package com.ireland.ager.Review.dto;
//
//import com.ireland.ager.account.entity.Account;
//import com.ireland.ager.product.entity.Category;
//import com.ireland.ager.product.entity.Product;
//import com.ireland.ager.product.entity.ProductStatus;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ReviewRequest {
//    String productName;
//    String productPrice;
//    String productDetail;
//    String category;
//
//    public Product toProduct(Account account,
//                             List<String> uploadImageUrl) {
//        Product product = new Product();
//        //FIXME Optional Account 반환 문제 해결해야한다.
//        product.addAccount(account);
//        product.setUrlList(images);
//        product.setProductDetail(this.productDetail);
//        product.setProductPrice(productPrice);
//        product.setProductViewCnt(0L);
//        product.setProductName(productName);
//        product.setStatus(ProductStatus.SALE);
//        product.setCategory(Category.valueOf(this.category));
//        return product;
//    }
//}

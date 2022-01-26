package com.ireland.ager.product.service;

import com.ireland.ager.account.service.AccountServiceImpl;
import com.ireland.ager.main.exception.NotFoundException;
import com.ireland.ager.account.exception.UnAuthorizedAccessException;
import com.ireland.ager.product.entity.Product;
import com.ireland.ager.product.entity.ProductStatus;
import com.ireland.ager.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TradeServiceImpl {

    private final ProductRepository productRepository;
    private final AccountServiceImpl accountService;


    public void isUpdated(long productId,
                          String accessToken,
                          String updateStatus) {

        Product product = productRepository.findById(productId).orElseThrow(NotFoundException::new);
        if (!product.getAccount().equals(accountService.findAccountByAccessToken(accessToken))) {
            throw new UnAuthorizedAccessException();
        }
        ProductStatus productStatus = findStatus(ProductStatus.valueOf(updateStatus));
        product.setStatus(productStatus);
        productRepository.save(product);
    }

    public ProductStatus checkStatus(long productId) {

        Product product = productRepository.findById(productId).orElseThrow(NotFoundException::new);
        return findStatus(product.getStatus());
    }

    public ProductStatus findStatus(ProductStatus status) {
        for (ProductStatus productStatus : ProductStatus.values()) {
            if (productStatus.equals(status)) {
                status = productStatus;
            }
        }
        return status;
    }
}

package com.ireland.ager.product.service;

import com.ireland.ager.account.service.AccountServiceImpl;
import com.ireland.ager.config.exception.NotFoundException;
import com.ireland.ager.config.exception.UnAuthorizedAccessException;
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

    //TODO : accessToken에 따라 buyer, seller 구분하고, 그에 따른 로직을 다르게 구성해야 한다.
    public void isUpdated(long productId,
                             String accessToken,
                             String updateStatus) {

        Product product = productRepository.findById(productId).orElseThrow(NotFoundException::new);
        if (!product.getAccount().equals(accountService.findAccountByAccessToken(accessToken))) {
            throw new UnAuthorizedAccessException();
        }
        ProductStatus productStatus=findStatus(ProductStatus.valueOf(updateStatus));
        product.setStatus(productStatus);
        productRepository.save(product);
    }

    // 현재 상태 체크
    public ProductStatus checkStatus(long productId) {
        // 해당 제품을 조회
        Product product = productRepository.findById(productId).orElseThrow(NotFoundException::new);
        // 제품의 상태 확인 후 해당하는 bool 값 리턴
        return findStatus(product.getStatus());
    }

    public ProductStatus findStatus(ProductStatus status) {
        for(ProductStatus productStatus:ProductStatus.values()) {
            if(productStatus.equals(status)) {
                status=productStatus;
            }
        }
        return status;
    }
}

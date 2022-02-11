package com.ireland.ager.account.service;

import com.ireland.ager.product.dto.response.ProductThumbResponse;
import com.ireland.ager.product.repository.ProductRepository;
import com.ireland.ager.product.service.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AccountInfoServiceImpl {
    private final AccountServiceImpl accountService;
    private final ProductRepository productRepository;
    public Slice<ProductThumbResponse> findSellsByAccountId(Long accountId, Pageable pageable) {
        return productRepository.findSellProductsByAccountId(accountId,pageable);
    }
}

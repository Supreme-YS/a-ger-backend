package com.ireland.ager.trade.service;

import com.ireland.ager.account.entity.Account;
import com.ireland.ager.account.repository.AccountRepository;
import com.ireland.ager.product.entity.Product;
import com.ireland.ager.product.entity.Status;
import com.ireland.ager.product.repository.ProductRepository;
import com.ireland.ager.trade.dto.request.TradeRequest;
import com.ireland.ager.trade.repository.TradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TradeServiceImpl {

    private final ProductRepository productRepository;
    private final AccountRepository accountRepository;

    private final TradeRepository tradeRepository;

    //TODO : accessToken에 따라 buyer, seller 구분하고, 그에 따른 로직을 다르게 구성해야 한다.
    public Boolean trade(TradeRequest tradeRequest, long productId, String accessToken) {

        // 제품 정보
        Optional<Product> product = productRepository.findById(productId);
        // 판매자 정보
        Account seller = product.get().getAccount();
        // 구매자 정보
        Optional<Account> buyer = accountRepository.findAccountByAccessToken(accessToken);

        if (product.get().getStatus().equals(Status.판매중)) {
            tradeRequest.setTradeStatus(true);
            return true;
        } else if (product.get().getStatus().equals(Status.예약중)) {
            tradeRequest.setTradeStatus(false);
            return false;
        } else {
            tradeRequest.setTradeStatus(false);
            return false;
        }
    }

}

package com.ireland.ager.trade.service;

import com.ireland.ager.account.entity.Account;
import com.ireland.ager.account.service.AccountServiceImpl;
import com.ireland.ager.chat.entity.MessageRoom;
import com.ireland.ager.chat.entity.ReviewStatus;
import com.ireland.ager.chat.repository.MessageRoomRepository;
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
    private final MessageRoomRepository messageRoomRepository;

    public void isUpdated(Long roomId,
                          String accessToken,
                          String updateStatus) {
        MessageRoom messageRoom = messageRoomRepository.findById(roomId).orElseThrow(NotFoundException::new);
        Product product = messageRoom.getProduct();
        Account accountByAccessToken = accountService.findAccountByAccessToken(accessToken);
        if (!product.getAccount().equals(accountByAccessToken)) {
            throw new UnAuthorizedAccessException();
        }
        product.setStatus(ProductStatus.valueOf(updateStatus));
        if(product.getStatus().equals(ProductStatus.COMPLETE)){
            messageRoom.setReviewStatus(ReviewStatus.SALE);
            messageRoomRepository.save(messageRoom);
        }
        productRepository.save(product);
        //REMARK: 예약중이거나 판매중일 경우 Trade에 저장합니다.
        //TODO: 예약중에서 판매중으로 바뀐다면,,? 그리고 판매 완료에서 리뷰로 넘어가야 한다면 필요한 정보들은?
    }
}

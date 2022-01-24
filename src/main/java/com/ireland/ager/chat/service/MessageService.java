package com.ireland.ager.chat.service;

import com.ireland.ager.account.entity.Account;
import com.ireland.ager.account.service.AccountServiceImpl;
import com.ireland.ager.account.service.AuthServiceImpl;
import com.ireland.ager.chat.dto.response.MessageSummaryResponse;
import com.ireland.ager.chat.entity.Message;
import com.ireland.ager.chat.entity.MessageRoom;
import com.ireland.ager.chat.repository.MessageRoomRepository;
import com.ireland.ager.config.exception.NotFoundException;
import com.ireland.ager.product.entity.Product;
import com.ireland.ager.product.service.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRoomRepository messageRoomRepository;
    private final AccountServiceImpl accountService;
    private final ProductServiceImpl productService;
    private final AuthServiceImpl authService;
    public MessageRoom insertMessage(Long roomId, Message message) {
        MessageRoom messageRoom = messageRoomRepository.findById(roomId).orElseThrow(NotFoundException::new);
        messageRoom.toAddMessage(messageRoom,message);
        return messageRoomRepository.save(messageRoom);
    }

    public MessageRoom insertRoom(Long productId, String accessToken) {
        Account buyerAccount = accountService.findAccountByAccessToken(accessToken);
        Product sellProduct = productService.findProductById(productId);
        Optional<MessageRoom> messageRoom = messageRoomRepository.findMessageRoomByProductAndBuyerId(sellProduct, buyerAccount);
        if(messageRoom.isPresent()) { //TODO 방을 만들때 이미 만들어져 있는 방은 생성하지 않아야 한다. messageDetailsResponse
            return messageRoom.get();
        }
        MessageRoom insertMessageRoom = new MessageRoom();
        insertMessageRoom.toCreateMessageRoom(sellProduct,buyerAccount);
        messageRoomRepository.save(insertMessageRoom);
        return insertMessageRoom;
    }

    public List<MessageSummaryResponse> findRoomByAccessToken(String accessToken) {
        Account account = accountService.findAccountByAccessToken(accessToken);
        List<MessageRoom> messageRoomsById = messageRoomRepository.findMessageRoomsBySellerIdOrBuyerId(account,account).orElseThrow(NotFoundException::new);
        List<MessageSummaryResponse> messageSummaryResponseList = new ArrayList<>();
        for(MessageRoom messageRoom: messageRoomsById) {
            messageSummaryResponseList.add(getMessageSummaryResponse(messageRoom,getAccountBySellOrBuy(messageRoom,account)));
        }
        //TODO seller일땐 buyer정보, buyer일땐 seller 정보 반환해야 한다.
        return messageSummaryResponseList;
    }
    public MessageSummaryResponse getMessageSummaryResponse(MessageRoom messageRoom,Account account) {
        return MessageSummaryResponse.toMessageSummaryResponse(messageRoom,account);
    }
    public Account getAccountBySellOrBuy(MessageRoom messageRoom,Account account) {
        log.info("Accessaccount:{}",account);
        log.info("Selleraccount:{}",messageRoom.getSellerId());
        log.info("Buyeraccount:{}",messageRoom.getBuyerId());
        if(account.equals(messageRoom.getBuyerId())) {
            return messageRoom.getSellerId();
        }
        else {
            return messageRoom.getBuyerId();
        }
    }
}

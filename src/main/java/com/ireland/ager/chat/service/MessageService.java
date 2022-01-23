package com.ireland.ager.chat.service;

import com.ireland.ager.account.entity.Account;
import com.ireland.ager.account.service.AccountServiceImpl;
import com.ireland.ager.account.service.AuthServiceImpl;
import com.ireland.ager.chat.dto.response.MessageRoomResponse;
import com.ireland.ager.chat.entity.Message;
import com.ireland.ager.chat.entity.MessageRoom;
import com.ireland.ager.chat.repository.MessageRepository;
import com.ireland.ager.chat.repository.MessageRoomRepository;
import com.ireland.ager.config.exception.NotFoundException;
import com.ireland.ager.product.entity.Product;
import com.ireland.ager.product.service.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRoomRepository messageRoomRepository;
    private final MessageRepository messageRepository;
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
        log.info("buyerId:{}",buyerAccount.getAccountId());
        log.info("sellerId:{}",sellProduct.getAccount().getAccountId());
        Optional<MessageRoom> messageRoom = messageRoomRepository.findMessageRoomByProductAndBuyerId(sellProduct, buyerAccount);
        if(messageRoom.isPresent()) { //TODO 방을 만들때 이미 만들어져 있는 방은 생성하지 않아야 한다.
            return messageRoom.get();
        }
        MessageRoom insertMessageRoom = new MessageRoom();
        insertMessageRoom.toCreateMessageRoom(sellProduct,buyerAccount);
        messageRoomRepository.save(insertMessageRoom);
        return insertMessageRoom;
    }

    public List<MessageRoom> findRoomByAccessToken(String accessToken) {
        Account account = accountService.findAccountByAccessToken(accessToken);
        Optional<List<MessageRoom>> messageRoomsById = messageRoomRepository.findMessageRoomsBySellerIdOrBuyerId(account,account);
        //null이어도 에러 처리를 할 필요는 없다.
        if(messageRoomsById.isPresent()) {
            getMessageRoomResponse(messageRoomsById.get());
        }
        return messageRoomsById.orElse(null);
    }

    public List<MessageRoomResponse> getMessageRoomResponse(List<MessageRoom> messageRoomsById) {

        return null;
    }
}

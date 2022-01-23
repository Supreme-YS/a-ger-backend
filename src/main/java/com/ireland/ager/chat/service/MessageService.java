package com.ireland.ager.chat.service;

import com.ireland.ager.account.entity.Account;
import com.ireland.ager.account.service.AccountServiceImpl;
import com.ireland.ager.chat.entity.Message;
import com.ireland.ager.chat.entity.MessageRoom;
import com.ireland.ager.chat.repository.MessageRoomRepository;
import com.ireland.ager.config.exception.NotFoundException;
import com.ireland.ager.product.entity.Product;
import com.ireland.ager.product.service.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRoomRepository messageRoomRepository;
    private final AccountServiceImpl accountService;
    private final ProductServiceImpl productService;
    public void insertMessage(Long roomId, Message message) {
        MessageRoom messageRoom = messageRoomRepository.findById(roomId).orElseThrow(NotFoundException::new);
        messageRoom.toAddMessage(messageRoom,message);
        messageRoomRepository.save(messageRoom);
    }

    public MessageRoom insertRoom(Long productId, Long buyerId) {
        Account buyerAccount = accountService.findAccountById(buyerId);
        Product sellProduct = productService.findProductById(productId);
        MessageRoom insertMessageRoom = new MessageRoom();
        insertMessageRoom.toCreateMessageRoom(sellProduct,buyerAccount);
        messageRoomRepository.save(insertMessageRoom);
        return insertMessageRoom;
    }
}

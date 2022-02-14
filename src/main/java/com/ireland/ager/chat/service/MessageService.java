package com.ireland.ager.chat.service;

import com.ireland.ager.account.entity.Account;
import com.ireland.ager.account.exception.UnAuthorizedAccessException;
import com.ireland.ager.account.service.AccountServiceImpl;
import com.ireland.ager.chat.dto.request.MessageRequest;
import com.ireland.ager.chat.dto.response.MessageDetailsResponse;
import com.ireland.ager.chat.dto.response.MessageSummaryResponse;
import com.ireland.ager.chat.dto.response.RoomCreateResponse;
import com.ireland.ager.chat.entity.Message;
import com.ireland.ager.chat.entity.MessageRoom;
import com.ireland.ager.chat.entity.RoomStatus;
import com.ireland.ager.chat.exception.UnAuthorizedChatException;
import com.ireland.ager.chat.repository.MessageRepository;
import com.ireland.ager.chat.repository.MessageRoomRepository;
import com.ireland.ager.main.exception.NotFoundException;
import com.ireland.ager.product.entity.Product;
import com.ireland.ager.product.service.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MessageService {
    private final MessageRoomRepository messageRoomRepository;
    private final MessageRepository messageRepository;
    private final AccountServiceImpl accountService;
    private final ProductServiceImpl productService;
    public MessageRoom findByRoomId(Long roomId) {
        return messageRoomRepository.findById(roomId).orElseThrow(NotFoundException::new);
    }
    public void deleteById(String accessToken,Long roomId) {
        Account accountByAccessToken=accountService.findAccountByAccessToken(accessToken);
        MessageRoom messageRoom=findByRoomId(roomId);
        MessageRoom messageRoomAfterDelete = deleteAccountWithMessageRoom(messageRoom, accountByAccessToken);
        if(messageRoomAfterDelete.getRoomStatus().equals(RoomStatus.EMPTY)) {
            messageRoomRepository.deleteById(roomId);
        }
    }
    public MessageRoom deleteAccountWithMessageRoom(MessageRoom messageRoom, Account account) {
        if(!(account.equals(messageRoom.getSellerId()) || account.equals(messageRoom.getBuyerId()))) {
            throw new UnAuthorizedAccessException();
        }
        messageRoom.updateRoomStatus(account);
        return messageRoomRepository.save(messageRoom);
    }

    public RoomCreateResponse insertRoom(Long productId, String accessToken) {
        Account buyerAccount = accountService.findAccountByAccessToken(accessToken);
        Product sellProduct = productService.findProductById(productId);
        if(buyerAccount.equals(sellProduct.getAccount())) {
            throw new UnAuthorizedChatException();
        }
        Optional<MessageRoom> messageRoom = messageRoomRepository.findMessageRoomByProductAndBuyerId(sellProduct, buyerAccount);
        if(messageRoom.isPresent()) {
            return RoomCreateResponse.toRoomCreateResponse(messageRoom.get());
        }
        MessageRoom insertMessageRoom = new MessageRoom();
        insertMessageRoom.toCreateMessageRoom(sellProduct,buyerAccount);
        MessageRoom savedRoom = messageRoomRepository.save(insertMessageRoom);
        //웰컴 메시지
        Message message = MessageRequest.toMessage(
                new MessageRequest(
                        savedRoom.getRoomId(), buyerAccount.getAccountId(), "상품을 구매하고 싶어요")
                , savedRoom);
        messageRepository.save(message);
        return RoomCreateResponse.toRoomCreateResponse(insertMessageRoom);
    }
    public MessageDetailsResponse roomEnterByAccessToken(String accessToken, Long roomId) {
        MessageRoom messageRoombyRoomId = messageRoomRepository.findMessageRoomByRoomId(roomId).orElseThrow(NotFoundException::new);
        Account accountByAccessToken = accountService.findAccountByAccessToken(accessToken);
        if(!(accountByAccessToken.equals(messageRoombyRoomId.getSellerId())
                || accountByAccessToken.equals(messageRoombyRoomId.getBuyerId()))) {
            throw new UnAuthorizedAccessException();
        }
        return MessageDetailsResponse.toMessageDetailsResponse(messageRoombyRoomId);
    }
    public Slice<MessageSummaryResponse> findRoomByAccessToken(String accessToken,Pageable pageable) {
        Account account = accountService.findAccountByAccessToken(accessToken);
        return messageRoomRepository.findMessageRoomsBySellerIdOrBuyerId(account,account,pageable);
    }


}

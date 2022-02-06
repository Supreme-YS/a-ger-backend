package com.ireland.ager.chat.service;

import com.ireland.ager.account.entity.Account;
import com.ireland.ager.account.service.AccountServiceImpl;
import com.ireland.ager.account.service.AuthServiceImpl;
import com.ireland.ager.chat.dto.response.MessageDetailsResponse;
import com.ireland.ager.chat.dto.response.MessageSummaryResponse;
import com.ireland.ager.chat.dto.response.RoomCreateResponse;
import com.ireland.ager.chat.entity.Message;
import com.ireland.ager.chat.entity.MessageRoom;
import com.ireland.ager.chat.repository.MessageRoomRepository;
import com.ireland.ager.main.exception.NotFoundException;
import com.ireland.ager.account.exception.UnAuthorizedAccessException;
import com.ireland.ager.chat.exception.UnAuthorizedChatException;
import com.ireland.ager.product.entity.Product;
import com.ireland.ager.product.service.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MessageService {
    private final MessageRoomRepository messageRoomRepository;
    private final AccountServiceImpl accountService;
    private final ProductServiceImpl productService;
    public MessageRoom insertMessage(Long roomId, Message message) {
        MessageRoom messageRoom = messageRoomRepository.findById(roomId).orElseThrow(NotFoundException::new);
        messageRoom.toAddMessage(messageRoom,message);
        return messageRoomRepository.save(messageRoom);
    }
    public MessageRoom findByRoomId(Long roomId) {
        return messageRoomRepository.findById(roomId).orElseThrow(NotFoundException::new);
    }
    public void deleteById(String accessToken,Long roomId) {
        Account accountByAccessToken=accountService.findAccountByAccessToken(accessToken);
        MessageRoom messageRoom=findByRoomId(roomId);
        //TODO: Room에 account 정보에 null 값을 넣는다.
        MessageRoom messageRoomAfterDelete = deleteAccountWithMessageRoom(messageRoom, accountByAccessToken);
        //REMARK: 둘 다 null 값이면 delete 처리를 한다.
        if(messageRoomAfterDelete.getRoomStatus()==0) {
            messageRoomRepository.deleteById(roomId);
        }
    }
    public MessageRoom deleteAccountWithMessageRoom(MessageRoom messageRoom, Account account) {
        if(!(account.equals(messageRoom.getSellerId()) || account.equals(messageRoom.getBuyerId()))) {
            //REMARK 권한이 없는 경우 에러 처리
            throw new UnAuthorizedAccessException();
        }
        if(messageRoom.getRoomStatus()==0) return messageRoom;
        if(messageRoom.getRoomStatus()!=3) { //FIXME: 1이나 2일때 buyer
            if(messageRoom.getRoomStatus()==2 && messageRoom.getSellerId().equals(account)) {
                messageRoom.setRoomStatus(0);
            }
            else if(messageRoom.getRoomStatus()==1 && messageRoom.getBuyerId().equals(account)) {
                messageRoom.setRoomStatus(0);
            }
            if(messageRoom.getSellerId().equals(account)) messageRoom.setRoomStatus(1);
            else messageRoom.setRoomStatus(2);
        }
        else {
            //3일떄 seller를 0할때 buyer를 0할때
            if(messageRoom.getSellerId().equals(account)) messageRoom.setRoomStatus(1);
            else messageRoom.setRoomStatus(2);
        }
        return messageRoomRepository.save(messageRoom);
    }

    public RoomCreateResponse insertRoom(Long productId, String accessToken) {
        Account buyerAccount = accountService.findAccountByAccessToken(accessToken);
        Product sellProduct = productService.findProductById(productId);
        //REMARK: product의 account와 채팅을 하려는 사용자가 같을때는 채팅방 개설이 되지 않아야 한다.
        if(buyerAccount.equals(sellProduct.getAccount())) {
            throw new UnAuthorizedChatException();
        }
        Optional<MessageRoom> messageRoom = messageRoomRepository.findMessageRoomByProductAndBuyerId(sellProduct, buyerAccount);
        if(messageRoom.isPresent()) { //TODO 방을 만들때 이미 만들어져 있는 방은 생성하지 않아야 한다. messageDetailsResponse
            return RoomCreateResponse.toRoomCreateResponse(messageRoom.get());
        }
        MessageRoom insertMessageRoom = new MessageRoom();
        insertMessageRoom.toCreateMessageRoom(sellProduct,buyerAccount);
        messageRoomRepository.save(insertMessageRoom);
        return RoomCreateResponse.toRoomCreateResponse(insertMessageRoom);
    }
    public MessageDetailsResponse roomEnterByAccessToken(String accessToken, Long roomId) {
        MessageRoom messageRoombyRoomId = messageRoomRepository.findMessageRoomWithMessageByRoomId(roomId).orElseThrow(NotFoundException::new);
        Account accountByAccessToken = accountService.findAccountByAccessToken(accessToken);
        //TODO: 권한이 있는 사용자만 채팅방에 입장할 수 있다.
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

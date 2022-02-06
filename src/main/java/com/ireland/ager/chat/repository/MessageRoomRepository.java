package com.ireland.ager.chat.repository;

import com.ireland.ager.account.entity.Account;
import com.ireland.ager.chat.entity.MessageRoom;
import com.ireland.ager.product.entity.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRoomRepository extends JpaRepository<MessageRoom, Long>, MessageRoomRepositoryCustom {
    Optional<MessageRoom> findMessageRoomByProductAndBuyerId(Product product,Account buyerId);

    @EntityGraph(attributePaths = {"messages"})
    Optional<MessageRoom> findMessageRoomWithMessageByRoomId(Long roomId);
}

package com.ireland.ager.chat.repository;

import com.ireland.ager.account.entity.Account;
import com.ireland.ager.chat.dto.request.MessageRequest;
import com.ireland.ager.chat.entity.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface MessageRepositoryCustom {
    Slice<Message> findMessagesByMessageRequestOrderByCreatedAtDesc(Long messageRoomId, Pageable pageable);
}

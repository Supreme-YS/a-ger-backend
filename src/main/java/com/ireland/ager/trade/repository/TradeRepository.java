package com.ireland.ager.trade.repository;

import com.ireland.ager.chat.entity.Message;
import com.ireland.ager.trade.entity.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeRepository extends JpaRepository<Trade, Long> {
}

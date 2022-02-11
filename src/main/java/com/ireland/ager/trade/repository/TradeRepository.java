package com.ireland.ager.trade.repository;

import com.ireland.ager.product.entity.Product;
import com.ireland.ager.product.repository.ProductRepositoryCustom;
import com.ireland.ager.trade.entity.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Long>, TradeRepositoryCustom {
}

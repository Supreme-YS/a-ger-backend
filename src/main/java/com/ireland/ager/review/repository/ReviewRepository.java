package com.ireland.ager.review.repository;

import com.ireland.ager.account.entity.Account;
import com.ireland.ager.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    List<Review> findAllBySellerId(Optional<Account> accountId);
}

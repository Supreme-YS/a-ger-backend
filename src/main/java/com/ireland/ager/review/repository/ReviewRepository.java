package com.ireland.ager.review.repository;

import com.ireland.ager.account.entity.Account;
import com.ireland.ager.review.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long>, ReviewRepositoryCustom {
    List<Review> findAllBySellerId(Optional<Account> accountId);
}

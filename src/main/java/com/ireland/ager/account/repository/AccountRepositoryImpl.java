package com.ireland.ager.account.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.ireland.ager.account.entity.QAccount.account;
import static com.ireland.ager.review.entity.QReview.review;

@Repository
@RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountRepositoryCustom{
    private final JPAQueryFactory queryFactory;
}

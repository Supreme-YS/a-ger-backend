package com.ireland.ager.board.repository;


import com.ireland.ager.product.dto.response.ProductThumbResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import static com.ireland.ager.board.entity.QComment.comment;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom{
    private final JPAQueryFactory queryFactory;
}

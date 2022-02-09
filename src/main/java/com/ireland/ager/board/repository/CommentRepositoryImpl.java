package com.ireland.ager.board.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.ireland.ager.board.entity.QComment.comment;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public Long countingCommentByBoardId(Long boardId) {
        return queryFactory
                .selectFrom(comment)
                .where(comment.boardId.boardId.eq(boardId))
                .fetchCount();
    }
}

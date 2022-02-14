package com.ireland.ager.board.repository;

import com.ireland.ager.board.dto.response.BoardSummaryResponse;
import com.ireland.ager.board.entity.Board;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

import static com.ireland.ager.board.entity.QBoard.board;
import static com.ireland.ager.board.entity.QComment.comment;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<BoardSummaryResponse> findAllBoardPageableOrderByCreatedAtDesc(String keyword, Pageable pageable) {
        JPAQuery<Board> boardJPAQuery = queryFactory
                .selectFrom(board)
                .where(keywordContains(keyword))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1); //limit보다 한 개 더 들고온다.
        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(board.getType(), board.getMetadata());
            boardJPAQuery.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC, pathBuilder.get(o.getProperty())));
        }
        List<Board> fetch = boardJPAQuery.fetch();
        List<BoardSummaryResponse> content = new ArrayList<>();
        for (Board board : fetch) {
            content.add(BoardSummaryResponse.toBoardSummaryResponse(board));
        }
        boolean hasNext = false;
        //마지막 페이지는 사이즈가 항상 작다.
        if (content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            hasNext = true;
        }
        return new SliceImpl<>(content, pageable, hasNext);
    }

    @Override
    public Slice<BoardSummaryResponse> findBoardsByAccountId(Long accountId, Pageable pageable) {
        JPAQuery<Board> boardQuery = queryFactory
                .selectFrom(board)
                .where(board.accountId.accountId.eq(accountId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1); //limit보다 한 개 더 들고온다.
        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(board.getType(), board.getMetadata());
            boardQuery.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC, pathBuilder.get(o.getProperty())));
        }
        List<Board> fetch = boardQuery.fetch();
        List<BoardSummaryResponse> content = new ArrayList<>();
        for (Board board : fetch) {
            Long countComment = queryFactory
                    .selectFrom(comment)
                    .where(comment.boardId.boardId.eq(board.getBoardId()))
                    .fetchCount();
            content.add(BoardSummaryResponse.toBoardSummaryResponse(board));
        }
        boolean hasNext = false;
        //마지막 페이지는 사이즈가 항상 작다.
        if (content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            hasNext = true;
        }
        return new SliceImpl<>(content, pageable, hasNext);
    }

    @Override
    public void addViewCntFromRedis(Long boardId, Long addCnt) {
        queryFactory
                .update(board)
                .set(board.boardViewCnt, addCnt)
                .where(board.boardId.eq(boardId))
                .execute();
    }

    @Override
    public Board addViewCnt(Long boardId) {
        queryFactory
                .update(board)
                .set(board.boardViewCnt, board.boardViewCnt.add(1))
                .where(board.boardId.eq(boardId))
                .execute();
        return queryFactory.selectFrom(board).where(board.boardId.eq(boardId)).fetchOne();
    }

    private BooleanExpression keywordContains(String keyword) {
        return ObjectUtils.isEmpty(keyword) ? null : board.title.contains(keyword);
    }
}

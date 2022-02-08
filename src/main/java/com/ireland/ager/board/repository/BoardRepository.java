package com.ireland.ager.board.repository;

import com.ireland.ager.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {
    @Query(value = "select b.boardViewCnt from Board as b where b.boardId = :boardId")
    Long findBoardViewCnt(Long boardId);
}

package com.ireland.ager.board.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ireland.ager.account.entity.Account;
import com.ireland.ager.config.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @ManyToOne
    @JsonIgnore
    private Account accountId;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    // 조회수
    private Long boardViewCnt;

    // @Setter 대신에 무분별한 setter사용 방지를 위해 @Builder 패턴을 사용한다.
//    @Builder
//    public Board(String title, String content, int viewCnt) {
//        this.title = title;
//        this.content = content;
//        this.viewCnt = viewCnt;
//    }

    public void addAccount(Account account) {
        this.accountId = account;
        this.accountId.getBoards().add(this);
    }

    public void addViewCnt(Board addBoard) {
        this.setBoardViewCnt(addBoard.getBoardViewCnt() + 1);
    }
}

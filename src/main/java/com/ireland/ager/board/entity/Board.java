package com.ireland.ager.board.entity;

import com.ireland.ager.account.entity.Account;
import com.ireland.ager.config.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Board extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Account accountId;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    // @Setter 대신에 무분별한 setter사용 방지를 위해 @Builder 패턴을 사용한다.
    @Builder
    public Board(Long id, Account accountId, String title, String content) {
        this.id = id;
        this.accountId = accountId;
        this.title = title;
        this.content = content;
    }

    public void addAccount(Account account) {
        this.accountId = account;
        this.accountId.getBoards().add(this);
    }
}

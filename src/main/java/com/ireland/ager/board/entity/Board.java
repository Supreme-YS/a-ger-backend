package com.ireland.ager.board.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ireland.ager.account.entity.Account;
import com.ireland.ager.config.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "account_id")
    private Account accountId;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    // 조회수
    private Long boardViewCnt;

    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL, mappedBy = "board", orphanRemoval = true)
    private List<BoardUrl> urlList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "boardId", fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<Comment>();

    public void addAccount(Account account) {
        this.accountId = account;
    }

    public void addUrl(BoardUrl url) {
        this.getUrlList().add(url);
        url.setBoard(this);
    }

    public void addViewCnt(Board addBoard) {
        this.setBoardViewCnt(addBoard.getBoardViewCnt() + 1);
    }
}

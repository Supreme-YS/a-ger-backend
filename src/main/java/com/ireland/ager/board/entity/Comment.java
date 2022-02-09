package com.ireland.ager.board.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ireland.ager.account.entity.Account;
import com.ireland.ager.config.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Board boardId;

    @ManyToOne
    @JsonIgnore
    private Account accountId;

    private String commentContent;

    public void addAccount(Account account) {
        this.accountId = account;
        this.accountId.getComments().add(this);
    }

}

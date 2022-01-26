package com.ireland.ager.Review.entity;


import com.ireland.ager.account.entity.Account;
import com.ireland.ager.config.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="review_id")
    private Long id;

    private String comment;

    private String buyer_id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    @Builder
    public Review(Long id, String comment, String buyer){
        this.id=id;
        this.comment=comment;
        this.buyer_id=buyer;
    }

    public void addAccount(Account account) {
        this.account = account;
        this.account.getReviews().add(this);
    }

}

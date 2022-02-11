package com.ireland.ager.account.entity;

import com.ireland.ager.board.entity.Board;
import com.ireland.ager.board.entity.Comment;
import com.ireland.ager.config.BaseEntity;
import com.ireland.ager.product.entity.Product;
import com.ireland.ager.review.entity.Review;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "accountId", callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
public class Account extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long accountId;
    String accountEmail;
    String profileNickname;
    String userName;
    String profileImageUrl;
    String accessToken;
    String refreshToken;

}
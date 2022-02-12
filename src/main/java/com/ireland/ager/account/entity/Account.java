package com.ireland.ager.account.entity;

import com.ireland.ager.config.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

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
    @Formula("(select avg(r.stars) from review r where r.seller_id=account_id)")
    Double avgStar;
}
package com.ireland.ager.account.entity;

import com.ireland.ager.config.BaseEntity;
import com.ireland.ager.product.entity.Product;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "account")
    Set<Product> products=new HashSet<>();
}
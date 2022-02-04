package com.ireland.ager.account.entity;

import com.ireland.ager.board.entity.Board;
import com.ireland.ager.config.BaseEntity;
import com.ireland.ager.product.entity.Product;
import com.ireland.ager.review.entity.Review;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@NamedEntityGraph(
        name = "Account.withProductAndUrl",
        attributeNodes = {
                @NamedAttributeNode(value = "products", subgraph = "urlList")
        },
        subgraphs = @NamedSubgraph(name = "urlList", attributeNodes = @NamedAttributeNode("urlList"))
)
@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "accountId", callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
public class Account extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue
    Long accountId;
    String accountEmail;
    String profileNickname;
    String userName;
    String profileImageUrl;
    String accessToken;
    String refreshToken;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "account")
    Set<Product> products = new HashSet<>();

    @OneToMany(mappedBy = "sellerId", cascade = CascadeType.ALL)
    private Set<Review> reviews = new LinkedHashSet<>();

    @OneToMany(mappedBy = "accountId", cascade = CascadeType.ALL)
    private List<Board> boards = new ArrayList<>();
//    private Set<Board> boards = new HashSet<>();

}
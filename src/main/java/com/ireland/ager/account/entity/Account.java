package com.ireland.ager.account.entity;

import com.ireland.ager.config.BaseEntity;
import com.ireland.ager.product.entity.Product;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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
}
package com.ireland.ager.account.entity;

import com.ireland.ager.config.BaseEntity;
import com.ireland.ager.product.entity.Product;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NamedEntityGraph(
    name="Account.withProductAndUrl",
    attributeNodes = {
        @NamedAttributeNode(value = "products", subgraph = "urlList")
    },
    subgraphs = @NamedSubgraph(name = "urlList", attributeNodes = @NamedAttributeNode("urlList"))
)
@Entity
@Getter
@Setter
@EqualsAndHashCode(of="accountId", callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Account extends BaseEntity {
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
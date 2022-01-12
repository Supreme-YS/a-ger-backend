package com.ireland.ager.product.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ireland.ager.account.entity.Account;
import com.ireland.ager.config.BaseEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Proxy;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of="productId", callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity {
    @Id @GeneratedValue
    private Long productId;

    private String productName;

    private String productPrice;

    private String productDetail;
    private Long productViewCnt;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private Status status = Status.판매중;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> urlList =new ArrayList<>();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    public void addAccount(Account updateAccount) {
        updateAccount.getProducts().add(this);
        this.setAccount(updateAccount);
    }
    public void addViewCnt(Product addProduct) {
        this.setProductViewCnt(addProduct.getProductViewCnt());
    }
}
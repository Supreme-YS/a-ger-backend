package com.ireland.ager.product.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ireland.ager.account.entity.Account;
import com.ireland.ager.config.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "productId", callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
public class Product extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    private String productName;

    private String productPrice;

    private String productDetail;
    private Long productViewCnt;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    private String thumbNailUrl;

    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL, mappedBy = "product", orphanRemoval = true)
    private List<Url> urlList = new ArrayList<>();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    public void addAccount(Account updateAccount) {
        updateAccount.getProducts().add(this);
        this.setAccount(updateAccount);
    }

    public void addViewCnt(Product addProduct) {
        this.setProductViewCnt(addProduct.getProductViewCnt() + 1);
    }

    public void addUrl(Url url) {
        this.getUrlList().add(url);
        url.setProduct(this);
    }
    public void deleteUrl(Url url) {
        url.setProduct(null);
        this.getUrlList().remove(url);
    }
}
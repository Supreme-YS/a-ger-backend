package com.ireland.ager.account.dto.response;

import com.ireland.ager.account.entity.Account;
import com.ireland.ager.product.entity.Product;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class OtherAccountResponse {
    Long accountId;
    String accountEmail;
    String profileNickname;
    String userName;
    String profileImageUrl;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    List<Product> productList = new ArrayList<>();

    public static OtherAccountResponse toOtherAccountResponse(Account account) {
        return OtherAccountResponse.builder()
            .profileNickname(account.getProfileNickname())
            .userName(account.getUserName())
            .accountEmail(account.getAccountEmail())
            .profileImageUrl(account.getProfileImageUrl())
            .accountId(account.getAccountId())
            .createdAt(account.getCreatedAt())
            .updatedAt(account.getUpdatedAt())
            .productList(account.getProductList())
            .build();
    }
}

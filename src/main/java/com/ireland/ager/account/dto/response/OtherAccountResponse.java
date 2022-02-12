package com.ireland.ager.account.dto.response;

import com.ireland.ager.account.entity.Account;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Builder
public class OtherAccountResponse {
    Long accountId;
    String accountEmail;
    String profileNickname;
    String profileImageUrl;
    Double avgStar;
    LocalDateTime createdAt;

    public static OtherAccountResponse toOtherAccountResponse(Account account) {
        return OtherAccountResponse.builder()
                .profileNickname(account.getProfileNickname())
                .accountEmail(account.getAccountEmail())
                .profileImageUrl(account.getProfileImageUrl())
                .accountId(account.getAccountId())
                .createdAt(account.getCreatedAt())
                .avgStar(Math.round(account.getAvgStar()*100)/100.0)
                .build();
    }
}

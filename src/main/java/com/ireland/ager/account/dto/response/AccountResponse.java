package com.ireland.ager.account.dto.response;

import com.ireland.ager.account.entity.Account;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
@Builder
public class AccountResponse {
    Long accountId;
    String accountEmail;
    String profileNickname;
    String userName;
    String profileImageUrl;
    String accessToken;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    public static AccountResponse toAccountResponse(Account account) {
        return AccountResponse.builder()
                .accessToken(account.getAccessToken())
                .profileNickname(account.getProfileNickname())
                .userName(account.getUserName())
                .accountEmail(account.getAccountEmail())
                .profileImageUrl(account.getProfileImageUrl())
                .accountId(account.getAccountId())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .build();
    }
}
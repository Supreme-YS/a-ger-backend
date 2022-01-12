package com.ireland.ager.account.dto.response;


import com.ireland.ager.account.entity.Account;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class AccountResponse {
    String accessToken;
    String profileNickname;
    String userName;
    String accountEmail;
    String profileImageUrl;

    public static AccountResponse of(Account account) {
        return AccountResponse.builder()
                .accessToken(account.getAccessToken())
                .profileNickname(account.getProfileNickname())
                .userName(account.getUserName())
                .accountEmail(account.getAccountEmail())
                .profileImageUrl(account.getProfileImageUrl())
                .build();
    }
}
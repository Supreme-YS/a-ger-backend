package com.ireland.ager.account.dto.request;

import com.ireland.ager.account.entity.Account;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AccountUpdateRequest {
    //가변값
    private String profileNickname;

    public Account toAccount(Account updateAccount) {
        updateAccount.setProfileNickname(this.profileNickname);
        updateAccount.setProfileImageUrl(this.profileImageUrl);
        return updateAccount;
    }
}
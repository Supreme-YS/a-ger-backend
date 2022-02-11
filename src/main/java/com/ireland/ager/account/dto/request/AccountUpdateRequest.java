package com.ireland.ager.account.dto.request;

import com.ireland.ager.account.entity.Account;
import lombok.Data;

@Data
public class AccountUpdateRequest {
    //가변값
    private String profileNickname;

    public Account toAccount(Account updateAccount) {
        updateAccount.setProfileNickname(this.profileNickname);
        return updateAccount;
    }
}
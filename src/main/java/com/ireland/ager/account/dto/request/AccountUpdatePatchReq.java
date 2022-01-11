package com.ireland.ager.account.dto.request;

import com.ireland.ager.account.entity.Account;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AccountUpdatePatchReq {

    String accountEmail;
    String profileNickname;
    String userName;
    String profileImageUrl;

    public Account toAccount(Account updateAccount) {
        updateAccount.setProfileNickname(this.profileNickname);
        updateAccount.setUserName(this.userName);
        return updateAccount;
    }
}
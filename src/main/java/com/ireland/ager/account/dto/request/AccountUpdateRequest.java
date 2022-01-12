package com.ireland.ager.account.dto.request;

import com.ireland.ager.account.entity.Account;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AccountUpdateRequest {

    String accountEmail;
    String profileNickname;
    String userName;
    String profileImageUrl;

    public Account toAccount(Account updateAccount) {
        updateAccount.setAccountEmail(this.accountEmail);
        updateAccount.setProfileNickname(this.profileNickname);
        updateAccount.setUserName(this.userName);
        return updateAccount;
    }
}
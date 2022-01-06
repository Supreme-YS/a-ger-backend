package com.ireland.ager.api.request.user;

import com.ireland.ager.db.entity.Account;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserUpdatePatchReq {

    String accountEmail;
    String profileNickname;
    String userName;
    String phoneNumber;
    String profileImageUrl;
    String profileDescription;
    String gender;
    String ageRange;

    public Account toUser(Account updateAccount) {
        updateAccount.setProfileNickname(this.profileNickname);
        updateAccount.setUserName(this.userName);
        updateAccount.setPhoneNumber(this.phoneNumber);
        // UserUpdatePatchReq의 profileImageUrl이 S3 서버의 URL을 갖고있을 수 있도록 수정이 필요
        // updateUser.setProfileImageUrl(this.profileImageUrl);
        updateAccount.setProfileDescription(this.profileDescription);
        updateAccount.setGender(this.gender);
        updateAccount.setAgeRange(this.ageRange);

        return updateAccount;
    }
}
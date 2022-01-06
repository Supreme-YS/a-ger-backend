package com.ireland.ager.account.service;

import com.ireland.ager.account.dto.request.UserUpdatePatchReq;
import com.ireland.ager.account.dto.response.UserRes;
import com.ireland.ager.account.entity.Account;

public interface UserService {
    UserRes insertUser(Account newAccount);
    UserRes updateUser(UserUpdatePatchReq userUpdatePatchReq);
    UserRes findUserByAccountEmail(String accountEmail);
    UserRes findUserByAccessToken(String accessToken);
}

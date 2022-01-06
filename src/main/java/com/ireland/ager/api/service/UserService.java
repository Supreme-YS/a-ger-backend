package com.ireland.ager.api.service;

import com.ireland.ager.api.request.user.UserUpdatePatchReq;
import com.ireland.ager.api.resoonse.user.UserRes;
import com.ireland.ager.db.entity.Account;

public interface UserService {
    UserRes insertUser(Account newAccount);
    UserRes updateUser(UserUpdatePatchReq userUpdatePatchReq);
    UserRes findUserByAccountEmail(String accountEmail);
    UserRes findUserByAccessToken(String accessToken);
}

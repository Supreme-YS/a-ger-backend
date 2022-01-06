package com.ireland.ager.api.service;

import com.ireland.ager.api.resoonse.user.KakaoUserRes;
import com.ireland.ager.api.resoonse.user.UserRes;

import java.util.HashMap;

public interface AuthService {
    String getKakaoLoginUrl();
    HashMap<String, String> getKakaoTokens(String code);
    KakaoUserRes getKakaoUserInfo(String accessToken);
    UserRes refreshTokensForExistUser(String email, String accessToken, String refreshToken);
    String accessTokenUpdate(String accountEmail);
    int isValidToken(String accessToken);
    void logout(UserRes userRes);
}

package com.ireland.ager.api.resoonse.user;


import com.ireland.ager.db.entity.Account;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
public class UserRes {
    //수정 시작
    String accountEmail;
    String profileNickname;
    String userName;
    String phoneNumber;
    String profileImageUrl;
    String profileDescription;
    String gender;
    String ageRange;
    String accessToken;
    String refreshToken;
    // showInfo, Reservation, Follow(Fan, artist), userVideo, video 리스트가 필드와 of method의 빌더에 추가되어야 한다.

    public static UserRes of(Account account) {
        return UserRes.builder()
                .accountEmail(account.getAccountEmail())
                .profileNickname(account.getProfileNickname())
                .userName(account.getUserName())
                .phoneNumber(account.getPhoneNumber())
                .profileImageUrl(account.getProfileImageUrl())
                .profileDescription(account.getProfileDescription())
                .gender(account.getGender())
                .ageRange(account.getAgeRange())
                .accessToken(account.getAccessToken())
                .refreshToken(account.getRefreshToken())
                .build();
    }

}

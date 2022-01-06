package com.ireland.ager.api.resoonse.user;

import com.ireland.ager.db.entity.Account;
import lombok.Data;

@Data
public class KakaoUserRes {

    private Integer id;
    private String connected_at;
    private Properties properties;
    private KakaoAccount kakao_account;

   @Data
    public class Properties {
        private String nickname;
        private String profile_image;
        private String thumbnail_image;
    }

   @Data
    public class KakaoAccount {
        private Boolean profile_needs_agreement;
        private Profile profile;
        private Boolean has_email;
        private Boolean email_needs_agreement;
        private Boolean is_email_valid;
        private Boolean is_email_verified;
        private String email;
        private String has_age_range;
        private boolean age_range_needs_agreement;
        private String age_range;
        private boolean birthday_needs_agreement;
        private String birthday;
        private String birthday_type;
        private boolean gender_needs_agreement;
        private boolean has_gender;
        private String gender;

        @Data
        public class Profile {
            private String nickname;
            private String thumbnail_image_url;
            private String profile_image_url;
        }
    }

    public Account toUser(String accessToken, String refreshToken) {
        Account account = new Account();
        if(this.kakao_account.email == null || this.kakao_account.email == "") account.setAccountEmail(String.valueOf(this.id));
        else account.setAccountEmail(this.kakao_account.email);

        account.setUserName(this.kakao_account.profile.nickname);
        account.setProfileImageUrl(this.kakao_account.profile.profile_image_url);
        account.setGender(this.kakao_account.gender);
        account.setAgeRange(this.kakao_account.age_range);
        account.setAccessToken(accessToken);
        account.setRefreshToken(refreshToken);

        return account;
    }
}

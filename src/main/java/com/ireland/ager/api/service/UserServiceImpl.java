package com.ireland.ager.api.service;

import com.ireland.ager.api.request.user.UserUpdatePatchReq;
import com.ireland.ager.api.resoonse.user.UserRes;
import com.ireland.ager.db.entity.Account;
import com.ireland.ager.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements  UserService{
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserRes findUserByAccountEmail(String accountEmail) {
        /**
         * @Method Name : findUserByAccountEmail
         * @작성자 : 김민권
         * @Method 설명 : accountEmail을 통해 user 정보를 반환한다.
         */
        Optional<Account> optionalUser = userRepository.findById(accountEmail);
        return optionalUser.map(user -> UserRes.of(user)).orElse(null);
    }

    @Override
    public UserRes findUserByAccessToken(String accessToken) {
        /**
         * @Method Name : findUserByAccessToken
         * @작성자 : 김민권
         * @Method 설명 : accessToken을 통해 user 정보를 반환한다.
         */
        Optional<Account> optionalUser = userRepository.findUserByAccessToken(accessToken);
        return optionalUser.map(user -> UserRes.of(user)).orElse(null);
    }

    @Override
    public UserRes insertUser(Account newAccount) {
        /**
         * @Method Name : insertUser
         * @작성자 : 김민권
         * @Method 설명 : 존재하지않는 회원에 대해서 각 토큰 값을 포함한 user를 삽입하고 반환한다.
         */
        newAccount.setProfileNickname(newAccount.getUserName());
        userRepository.save(newAccount);
        return UserRes.of(newAccount);
    }

    @Override
    public UserRes updateUser(UserUpdatePatchReq userUpdatePatchReq) {
        /**
         * @Method Name : updateUser
         * @작성자 : 김민권
         * @Method 설명 : 회원의 정보를 업데이트한다.
         */
        Optional<Account> optionalUpdateUser = userRepository.findById(userUpdatePatchReq.getAccountEmail());
        Account updatedAccount = optionalUpdateUser.map(user -> userUpdatePatchReq.toUser(user)).orElse(null);
        if(updatedAccount != null) userRepository.save(updatedAccount);

        return UserRes.of(updatedAccount);
    }
}

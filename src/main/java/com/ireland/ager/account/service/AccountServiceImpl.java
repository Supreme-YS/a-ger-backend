package com.ireland.ager.account.service;

import com.ireland.ager.account.dto.request.AccountUpdateRequest;
import com.ireland.ager.account.dto.response.AccountAllResponse;
import com.ireland.ager.account.dto.response.AccountResponse;
import com.ireland.ager.account.dto.response.OtherAccountResponse;
import com.ireland.ager.account.entity.Account;
import com.ireland.ager.account.exception.UnAuthorizedAccessException;
import com.ireland.ager.account.repository.AccountRepository;
import com.ireland.ager.main.exception.NotFoundException;
import com.ireland.ager.main.service.UploadServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AccountServiceImpl {
    private final AccountRepository accountRepository;
    private final RedisTemplate redisTemplate;
    private final UploadServiceImpl uploadService;

    public Account findAccountByAccountEmail(String accountEmail) {
        return accountRepository.findAccountByAccountEmail(accountEmail).orElse(null);
    }

    public Account findAccountById(Long accountId) {
        return accountRepository.findById(accountId).orElseThrow(NotFoundException::new);
    }

    public Account findAccountByAccessToken(String accessToken) {
        return accountRepository.findAccountByAccessToken(accessToken).orElseThrow(NotFoundException::new);
    }

    public Account findAccountWithProductById(Long accountId) {
        return accountRepository.findAccountWithProductByAccountId(accountId).orElseThrow(NotFoundException::new);
    }

    public AccountAllResponse findMyAccountByAccessToken(String accessToken) {
        return AccountAllResponse.toAccountAllResponse(findAccountByAccessToken(accessToken));
    }

    public OtherAccountResponse findOtherAccountByAccountId(Long accountId) {
        return OtherAccountResponse.toOtherAccountResponse(findAccountWithProductById(accountId));
    }

    public AccountResponse insertAccount(Account newAccount) {
        accountRepository.save(newAccount);
        return AccountResponse.toAccountResponse(newAccount);
    }

    public AccountAllResponse updateAccount(String accessToken, Long accountId,
                                            AccountUpdateRequest accountUpdateRequest,
                                            MultipartFile multipartFile) throws IOException {
        Account optionalUpdateAccount = findAccountByAccessToken(accessToken);
        if (!(optionalUpdateAccount.getAccountId().equals(accountId))) {
            throw new UnAuthorizedAccessException();
        }
        Account updatedAccount = accountUpdateRequest.toAccount(optionalUpdateAccount);

        if (!(multipartFile.isEmpty())) {
            String uploadImg = uploadService.uploadImg(multipartFile);
            updatedAccount.setProfileImageUrl(uploadImg);
        }
        accountRepository.save(updatedAccount);
        return AccountAllResponse.toAccountAllResponse(updatedAccount);
    }

    public void deleteAccount(String accessToken, Long accountId) {
        Account accountByAccessToken = findAccountByAccessToken(accessToken);
        if (!(accountByAccessToken.getAccountId().equals(accountId))) {
            // 삭제하고자 하는 사람과 현재 토큰 주인이 다르면 False
            throw new UnAuthorizedAccessException();
        }
        //HINT: redis 최근 검색어도 함께 추가
        String key = "search::" + accountId;
        redisTemplate.delete(key);
        accountRepository.deleteById(accountId);
    }
}


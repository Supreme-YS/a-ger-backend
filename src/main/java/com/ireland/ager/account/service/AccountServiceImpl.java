package com.ireland.ager.account.service;

import com.ireland.ager.account.dto.request.AccountUpdateRequest;
import com.ireland.ager.account.dto.response.MyAccountResponse;
import com.ireland.ager.account.dto.response.OtherAccountResponse;
import com.ireland.ager.account.entity.Account;
import com.ireland.ager.account.exception.UnAuthorizedAccessException;
import com.ireland.ager.account.repository.AccountRepository;
import com.ireland.ager.main.exception.NotFoundException;
import com.ireland.ager.main.service.UploadServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

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

    @Cacheable("account")
    public Account findAccountByAccessToken(String accessToken) {
        return accountRepository.findAccountByAccessToken(accessToken).orElseThrow(NotFoundException::new);
    }

    public MyAccountResponse insertAccount(Account newAccount) {
        accountRepository.save(newAccount);
        return MyAccountResponse.toAccountResponse(newAccount);
    }

    public MyAccountResponse updateAccount(String accessToken, Long accountId,
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
        return MyAccountResponse.toAccountResponse(updatedAccount);
    }

    public void deleteAccount(String accessToken, Long accountId) {
        Account accountByAccessToken = findAccountByAccessToken(accessToken);
        if (!(accountByAccessToken.getAccountId().equals(accountId))) {
            // ??????????????? ?????? ????????? ?????? ?????? ????????? ????????? False
            throw new UnAuthorizedAccessException();
        }
        //HINT: redis ?????? ???????????? ?????? ??????
        String key = "search::" + accountId;
        redisTemplate.delete(key);
        accountRepository.deleteById(accountId);
    }
}


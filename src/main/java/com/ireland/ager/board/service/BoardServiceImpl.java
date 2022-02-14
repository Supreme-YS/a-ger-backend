package com.ireland.ager.board.service;

import com.ireland.ager.account.entity.Account;
import com.ireland.ager.account.exception.UnAuthorizedAccessException;
import com.ireland.ager.account.service.AccountServiceImpl;
import com.ireland.ager.board.dto.request.BoardRequest;
import com.ireland.ager.board.dto.response.BoardResponse;
import com.ireland.ager.board.dto.response.BoardSummaryResponse;
import com.ireland.ager.board.entity.Board;
import com.ireland.ager.board.entity.BoardUrl;
import com.ireland.ager.board.exception.InvalidBoardDetailException;
import com.ireland.ager.board.exception.InvalidBoardTitleException;
import com.ireland.ager.board.repository.BoardRepository;
import com.ireland.ager.main.exception.NotFoundException;
import com.ireland.ager.main.service.UploadServiceImpl;
import com.ireland.ager.product.exception.InvaildUploadException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.Duration;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BoardServiceImpl {
    private final BoardRepository boardRepository;
    private final AccountServiceImpl accountService;
    private final UploadServiceImpl uploadService;
    private final RedisTemplate redisTemplate;

    public BoardResponse createPost(String accessToken,
                                    BoardRequest boardRequest,
                                    List<MultipartFile> multipartFile) throws IOException {
        Account account = accountService.findAccountByAccessToken(accessToken);
        List<String> uploadImagesUrl = uploadService.uploadImages(multipartFile);
        Board newPost = boardRepository.save(BoardRequest.toBoard(boardRequest, account, uploadImagesUrl));
        return BoardResponse.toBoardResponse(newPost, account);
    }

    public BoardResponse updatePost(String accessToken,
                                    Long boardId,
                                    BoardRequest boardRequest,
                                    List<MultipartFile> multipartFile) {
        Board board = findPostById(boardId);
        Account account = accountService.findAccountByAccessToken(accessToken);
        if (!(account.equals(board.getAccountId()))) {
            throw new UnAuthorizedAccessException();
        }
        validateFileExists(multipartFile);
        List<BoardUrl> currentFileImageUrlList = board.getUrlList();
        uploadService.deleteBoard(currentFileImageUrlList);
        board.deleteUrl();
        List<String> updateFileImageUrlList = new ArrayList<>();
        try {
            updateFileImageUrlList = uploadService.uploadImages(multipartFile);
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
        Board toBoardUpdate = boardRequest.toBoardUpdate(board, updateFileImageUrlList);
        boardRepository.save(toBoardUpdate);
        return BoardResponse.toBoardResponse(toBoardUpdate, account);
    }

    public void deletePost(String accessToken, Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(NotFoundException::new);
        Account account = accountService.findAccountByAccessToken(accessToken);
        if (!(account.equals(board.getAccountId()))) {
            throw new UnAuthorizedAccessException();
        }
        uploadService.deleteBoard(board.getUrlList());
        boardRepository.deleteById(board.getBoardId());
    }

    @Cacheable(value = "board")
    public Board findPostById(Long boardId) {
        return boardRepository.addViewCnt(boardId);
    }

    public void addViewCntToRedis(Long boardId) {
        String key = "boardViewCnt::" + boardId;

        ValueOperations valueOperations = redisTemplate.opsForValue();
        if (valueOperations.get(key) == null)
            valueOperations.set(
                    key,
                    String.valueOf(boardRepository.findBoardViewCnt(boardId)),
                    Duration.ofMinutes(5));
        else
            valueOperations.increment(key);
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    public void deleteViewCntCacheFromRedis() {
        Set<String> redisKeys = redisTemplate.keys("boardViewCnt*");
        Iterator<String> it = redisKeys.iterator();
        while (it.hasNext()) {
            String data = it.next();
            Long boardId = Long.parseLong(data.split("::")[1]);
            Long viewCnt = Long.parseLong((String) redisTemplate.opsForValue().get(data));
            boardRepository.addViewCntFromRedis(boardId, viewCnt);
            redisTemplate.delete(data);
            redisTemplate.delete("board::" + boardId);
        }
    }

    public Slice<BoardSummaryResponse> findBoardAllByCreatedAtDesc(String keyword, Pageable pageable) {
        return boardRepository.findAllBoardPageableOrderByCreatedAtDesc(keyword, pageable);
    }

    public void validateFileExists(List<MultipartFile> file) {
        if (file.isEmpty())
            throw new InvaildUploadException();
    }

    public void validateUploadForm(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(objectError -> {
                String errorCode = objectError.getDefaultMessage();
                switch (Objects.requireNonNull(errorCode)) {
                    case "3110":
                        throw new InvalidBoardTitleException();
                    case "3130":
                        throw new InvalidBoardDetailException();
                }
            });
        }
    }
}

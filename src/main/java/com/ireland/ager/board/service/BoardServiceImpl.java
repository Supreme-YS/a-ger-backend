package com.ireland.ager.board.service;

import com.ireland.ager.account.entity.Account;
import com.ireland.ager.account.exception.UnAuthorizedAccessException;
import com.ireland.ager.account.service.AccountServiceImpl;
import com.ireland.ager.board.dto.request.BoardRequest;
import com.ireland.ager.board.dto.response.BoardResponse;
import com.ireland.ager.board.dto.response.BoardSummaryResponse;
import com.ireland.ager.board.entity.Board;
import com.ireland.ager.board.entity.BoardUrl;
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
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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

        String thumbNailUrl = uploadService.makeThumbNail(multipartFile.get(0));

        List<String> uploadImagesUrl = uploadService.uploadImages(multipartFile);
        Board newPost = boardRepository.save(BoardRequest.toBoard(boardRequest, account, uploadImagesUrl));
        return BoardResponse.toBoardResponse(newPost, account);
    }

    public BoardResponse updatePost(String accessToken,
                                    Long boardId,
                                    BoardRequest boardRequest,
                                    List<MultipartFile> multipartFile) throws IOException {
        Board board = boardRepository.findById(boardId).orElseThrow(NotFoundException::new);
        Account account = accountService.findAccountByAccessToken(accessToken);
        if (!(account.equals(board.getAccountId()))) {
            throw new UnAuthorizedAccessException();
        }
        validateFileExists(multipartFile);
        List<BoardUrl> currentFileImageUrlList = board.getUrlList();
        uploadService.deleteBoard(currentFileImageUrlList);

        for (Iterator<BoardUrl> it = board.getUrlList().iterator(); it.hasNext(); ) {
            BoardUrl url = it.next();
            url.setBoard(null);
            it.remove();
        }
        for (BoardUrl url : board.getUrlList()) {
            url.setBoard(null);
        }
        List<String> updateFileImageUrlList = new ArrayList<>();
        try {
            updateFileImageUrlList = uploadService.uploadImages(multipartFile);
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }

        Account accountById = accountService.findAccountById(board.getAccountId().getAccountId());
        Board toBoardUpdate = boardRequest.toBoardUpdate(board, updateFileImageUrlList);
        boardRepository.save(toBoardUpdate);
        return BoardResponse.toBoardResponse(toBoardUpdate, accountById);
    }

    public void deletePost(String accessToken, Long boardId) throws IOException {
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
        String key = "boardViewCnt::"+boardId;

        ValueOperations valueOperations = redisTemplate.opsForValue();
        if(valueOperations.get(key)==null)
            valueOperations.set(
                    key,
                    String.valueOf(boardRepository.findBoardViewCnt(boardId)),
                    Duration.ofMinutes(5));
        else
            valueOperations.increment(key);
        log.info("value:{}",valueOperations.get(key));
    }

    //hint 스케줄러로 쌓인 조회수 캐시들 제거 3분마다 실행
    @Scheduled(cron = "0 0/3 * * * ?")
    public void deleteViewCntCacheFromRedis() {
        Set<String> redisKeys = redisTemplate.keys("boardViewCnt*");
        Iterator<String> it = redisKeys.iterator();
        while (it.hasNext()) {
            String data = it.next();
            Long boardId = Long.parseLong(data.split("::")[1]);
            Long viewCnt = Long.parseLong((String) redisTemplate.opsForValue().get(data));
            boardRepository.addViewCntFromRedis(boardId,viewCnt);
            redisTemplate.delete(data);
            redisTemplate.delete("board::"+boardId);
        }
    }

    public Slice<BoardSummaryResponse> findBoardAllByCreatedAtDesc(String keyword, Pageable pageable) {
        return boardRepository.findAllBoardPageableOrderByCreatedAtDesc(keyword, pageable);
    }

    public void validateFileExists(List<MultipartFile> file) {
        if (file.isEmpty())
            throw new InvaildUploadException();
    }
}

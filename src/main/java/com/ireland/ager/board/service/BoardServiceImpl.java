package com.ireland.ager.board.service;

import com.ireland.ager.account.entity.Account;
import com.ireland.ager.account.exception.UnAuthorizedAccessException;
import com.ireland.ager.account.service.AccountServiceImpl;
import com.ireland.ager.board.dto.request.BoardRequest;
import com.ireland.ager.board.dto.response.BoardResponse;
import com.ireland.ager.board.entity.Board;
import com.ireland.ager.board.entity.BoardUrl;
import com.ireland.ager.board.repository.BoardRepository;
import com.ireland.ager.main.exception.NotFoundException;
import com.ireland.ager.product.exception.InvaildUploadException;
import com.ireland.ager.product.service.UploadServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
        Board newPost = boardRepository.save(BoardRequest.toBoard(boardRequest, account, uploadImagesUrl, thumbNailUrl));
        return BoardResponse.toBoardResponse(newPost);
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
        String currentFileThumbnailUrl = board.getThumbNailUrl();
        uploadService.deleteBoard(currentFileImageUrlList, currentFileThumbnailUrl);

        for (Iterator<BoardUrl> it = board.getUrlList().iterator(); it.hasNext(); ) {
            BoardUrl url = it.next();
            url.setBoard(null);
            it.remove();
        }
        for (BoardUrl url : board.getUrlList()) {
            url.setBoard(null);
        }
        MultipartFile firstImage = multipartFile.get(0);
        List<String> updateFileImageUrlList = new ArrayList<>();
        try {
            updateFileImageUrlList = uploadService.uploadImages(multipartFile);
            board.setThumbNailUrl(uploadService.makeThumbNail(firstImage));
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }

        Account accountById = accountService.findAccountById(board.getAccountId().getAccountId());
        Board toBoardUpdate = boardRequest.toBoardUpdate(board, accountById, updateFileImageUrlList);
        boardRepository.save(toBoardUpdate);
        return BoardResponse.toBoardResponse(toBoardUpdate);
    }

    public void deletePost(String accessToken, Long boardId) throws IOException {
        Board board = boardRepository.findById(boardId).orElseThrow(NotFoundException::new);
        Account account = accountService.findAccountByAccessToken(accessToken);
        if (!(account.equals(board.getAccountId()))) {
            throw new UnAuthorizedAccessException();
        }
        uploadService.deleteBoard(board.getUrlList(), board.getThumbNailUrl());
        boardRepository.deleteById(board.getBoardId());
    }

    @Cacheable(value = "board")
    public Board findPost(Long boardId) {
        log.info("boardCache");
        return boardRepository.findById(boardId).orElseThrow(NotFoundException::new);
    }

    public BoardResponse findPostById(String accessToken, Long boardId) {
        Account account = accountService.findAccountByAccessToken(accessToken);
        Board board = findPost(boardId);
        if (!(account.equals(board.getAccountId()))) {
            board.addViewCnt(board);
            return BoardResponse.toOtherBoard(board);
        }
        board.addViewCnt(board);
        return BoardResponse.toBoardResponse(board);
    }

    public Slice<BoardResponse> findBoardAllByCreatedAtDesc(String keyword, Pageable pageable) {
        return boardRepository.findAllBoardPageableOrderByCreatedAtDesc(keyword, pageable);
    }

    public void validateFileExists(List<MultipartFile> file) {
        if (file.isEmpty())
            throw new InvaildUploadException();
    }
}

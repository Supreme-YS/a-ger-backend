package com.ireland.ager.board.controller;

import com.ireland.ager.account.entity.Account;
import com.ireland.ager.account.service.AccountServiceImpl;
import com.ireland.ager.board.dto.request.BoardRequest;
import com.ireland.ager.board.dto.response.BoardResponse;
import com.ireland.ager.board.entity.Board;
import com.ireland.ager.board.service.BoardServiceImpl;
import com.ireland.ager.main.common.CommonResult;
import com.ireland.ager.main.common.SingleResult;
import com.ireland.ager.main.common.service.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/board")
@CrossOrigin(value = {"*"}, maxAge = 6000)
public class BoardController {

    private final ResponseService responseService;
    private final BoardServiceImpl boardService;
    private final AccountServiceImpl accountService;

    @GetMapping("/{boardId}")
    public ResponseEntity<SingleResult<BoardResponse>> findBoardById(@PathVariable Long boardId,
                                                                     @RequestHeader("Authorization") String accessToken) {

        String[] splitToken = accessToken.split(" ");
        Account account = accountService.findAccountByAccessToken(splitToken[1]);
        Board board = boardService.findPostById(boardId);
        boardService.addViewCntToRedis(boardId);
        return new ResponseEntity<>(responseService.getSingleResult(BoardResponse.toBoardResponse(board, account)), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<SingleResult<BoardResponse>> createPost(@RequestHeader("Authorization") String accessToken,
                                                                  @RequestPart(value = "board") @Valid BoardRequest boardRequest,
                                                                  @RequestPart(value = "file") List<MultipartFile> multipartFile,
                                                                  BindingResult bindingResult) throws IOException {

        boardService.validateUploadForm(bindingResult);
        boardService.validateFileExists(multipartFile);
        String[] splitToken = accessToken.split(" ");
        BoardResponse boardResponse = boardService.createPost(splitToken[1], boardRequest, multipartFile);
        return new ResponseEntity<>(responseService.getSingleResult(boardResponse), HttpStatus.CREATED);
    }

    @PatchMapping("/{boardId}")
    public ResponseEntity<SingleResult<BoardResponse>> updatePost(@RequestHeader("Authorization") String accessToken,
                                                                  @RequestPart(value = "board") BoardRequest boardRequest,
                                                                  @RequestPart(value = "file") List<MultipartFile> multipartFile,
                                                                  @PathVariable(value = "boardId") Long boardId) throws IOException {
        String[] splitToken = accessToken.split(" ");
        BoardResponse boardResponse = boardService.updatePost(splitToken[1], boardId, boardRequest, multipartFile);
        return new ResponseEntity<>(responseService.getSingleResult(boardResponse), HttpStatus.OK);
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<CommonResult> deletePost(@RequestHeader("Authorization") String accessToken,
                                                   @PathVariable(value = "boardId") Long boardId) throws IOException {
        String[] splitToken = accessToken.split(" ");
        boardService.deletePost(splitToken[1], boardId);
        return new ResponseEntity<>(responseService.getSuccessResult(), HttpStatus.OK);
    }
}

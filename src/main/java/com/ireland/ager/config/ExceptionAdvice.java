package com.ireland.ager.config;

import com.ireland.ager.account.exception.ExpiredAccessTokenException;
import com.ireland.ager.account.exception.NotFoundTokenException;
import com.ireland.ager.account.exception.UnAuthorizedAccessException;
import com.ireland.ager.account.exception.UnAuthorizedTokenException;
import com.ireland.ager.board.exception.InvalidBoardDetailException;
import com.ireland.ager.board.exception.InvalidBoardTitleException;
import com.ireland.ager.chat.exception.UnAuthorizedChatException;
import com.ireland.ager.main.common.CommonResponse;
import com.ireland.ager.main.common.CommonResult;
import com.ireland.ager.main.common.service.ResponseService;
import com.ireland.ager.main.exception.IntenalServerErrorException;
import com.ireland.ager.main.exception.NotFoundException;
import com.ireland.ager.product.exception.*;
import com.ireland.ager.review.exception.DuplicateReviewException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionAdvice {
    private final ResponseService responseService;

    @ExceptionHandler(ExpiredAccessTokenException.class)
    public ResponseEntity<CommonResult> expiredAccessTokenException(ExpiredAccessTokenException e) {
        CommonResult commonResult = responseService.getFailResult(CommonResponse.EXPIREDACCESSTOKEN);
        return new ResponseEntity<>(commonResult, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<CommonResult> notFoundException(NotFoundException e) {
        CommonResult commonResult = responseService.getFailResult(CommonResponse.NOTFOUND);
        return new ResponseEntity<>(commonResult, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnAuthorizedAccessException.class)
    public ResponseEntity<CommonResult> unAuthorizedAccessException(UnAuthorizedAccessException e) {
        CommonResult commonResult = responseService.getFailResult(CommonResponse.UNAUTHORIZEDACCESS);
        return new ResponseEntity<>(commonResult, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvaildUploadException.class)
    public ResponseEntity<CommonResult> invaildUploadException(InvaildUploadException e) {
        CommonResult commonResult = responseService.getFailResult(CommonResponse.INVALIDUPLOAD);
        return new ResponseEntity<>(commonResult, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvaildDataException.class)
    public ResponseEntity<CommonResult> invaildDataException(InvaildDataException e) {
        CommonResult commonResult = responseService.getFailResult(CommonResponse.INVALIDDATA);
        return new ResponseEntity<>(commonResult, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvaildFileExtensionException.class)
    public ResponseEntity<CommonResult> invaildFileExtensionException(InvaildFileExtensionException e) {
        CommonResult commonResult = responseService.getFailResult(CommonResponse.INVALIDFILEEXTENSION);
        return new ResponseEntity<>(commonResult, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IntenalServerErrorException.class)
    public ResponseEntity<CommonResult> intenalServerErrorException(IntenalServerErrorException e) {
        CommonResult commonResult = responseService.getFailResult(CommonResponse.INTERNALSERVERERROR);
        return new ResponseEntity<>(commonResult, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnAuthorizedTokenException.class)
    public ResponseEntity<CommonResult> unAuthorizedTokenException(UnAuthorizedTokenException e) {
        CommonResult commonResult = responseService.getFailResult(CommonResponse.UNAUTHORIZEDTOKEN);
        return new ResponseEntity<>(commonResult, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundTokenException.class)
    public ResponseEntity<CommonResult> unAuthorizedTokenException(NotFoundTokenException e) {
        CommonResult commonResult = responseService.getFailResult(CommonResponse.NOTFOUNDTOKEN);
        return new ResponseEntity<>(commonResult, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UnAuthorizedChatException.class)
    public ResponseEntity<CommonResult> unAuthorizedChatException(UnAuthorizedChatException e) {
        CommonResult commonResult = responseService.getFailResult(CommonResponse.UNAUTHORIZEDCHAT);
        return new ResponseEntity<>(commonResult, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvaildFormException.class)
    public ResponseEntity<CommonResult> invaildFormException(InvaildFormException e) {
        CommonResult commonResult = responseService.getFailResult(CommonResponse.INVALIDFORM);
        return new ResponseEntity<>(commonResult, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvaildProductTitleException.class)
    public ResponseEntity<CommonResult> invaildProductTitleException(InvaildProductTitleException e) {
        CommonResult commonResult = responseService.getFailResult(CommonResponse.INVALIDPRODUCTTITLE);
        return new ResponseEntity<>(commonResult, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvaildProductPriceException.class)
    public ResponseEntity<CommonResult> invaildProductPriceException(InvaildProductPriceException e) {
        CommonResult commonResult = responseService.getFailResult(CommonResponse.INVALIDPRODUCTPRICE);
        return new ResponseEntity<>(commonResult, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvaildProductDetailException.class)
    public ResponseEntity<CommonResult> invaildProductDetailException(InvaildProductDetailException e) {
        CommonResult commonResult = responseService.getFailResult(CommonResponse.INVALIDPRODUCTDETAIL);
        return new ResponseEntity<>(commonResult, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvaildProductCategoryException.class)
    public ResponseEntity<CommonResult> InvaildProductCategoryException(InvaildProductCategoryException e) {
        CommonResult commonResult = responseService.getFailResult(CommonResponse.INVALIDPRODUCTCATEGORY);
        return new ResponseEntity<>(commonResult, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvaildProductStatusException.class)
    public ResponseEntity<CommonResult> InvaildProductStatusException(InvaildProductStatusException e) {
        CommonResult commonResult = responseService.getFailResult(CommonResponse.INVALIDPRODUCTSTATUS);
        return new ResponseEntity<>(commonResult, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateReviewException.class)
    public ResponseEntity<CommonResult> DuplicateReviewException(DuplicateReviewException e) {
        CommonResult commonResult = responseService.getFailResult(CommonResponse.DUPLICATEREVIEW);
        return new ResponseEntity<>(commonResult, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(InvalidBoardTitleException.class)
    public ResponseEntity<CommonResult> InvalidBoardTitleException(InvalidBoardTitleException e) {
        CommonResult commonResult = responseService.getFailResult(CommonResponse.INVALIDBOARDTITLE);
        return new ResponseEntity<>(commonResult, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(InvalidBoardDetailException.class)
    public ResponseEntity<CommonResult> InvalidBoardTitleException(InvalidBoardDetailException e) {
        CommonResult commonResult = responseService.getFailResult(CommonResponse.INVALIDBOARDDETAIL);
        return new ResponseEntity<>(commonResult, HttpStatus.BAD_REQUEST);
    }
}

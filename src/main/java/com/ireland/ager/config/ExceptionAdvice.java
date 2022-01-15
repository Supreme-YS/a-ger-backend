package com.ireland.ager.config;

import com.ireland.ager.config.exception.*;
import com.ireland.ager.main.common.CommonResponse;
import com.ireland.ager.main.common.CommonResult;
import com.ireland.ager.main.common.service.ResponseService;
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
        CommonResult commonResult=responseService.getFailResult(CommonResponse.EXPIREDACCESSTOKEN);
        return new ResponseEntity<>(commonResult,HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<CommonResult> notFoundException(NotFoundException e) {
        CommonResult commonResult=responseService.getFailResult(CommonResponse.NOTFOUND);
        return new ResponseEntity<>(commonResult,HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(UnAuthorizedAccessException.class)
    public ResponseEntity<CommonResult> unAuthorizedAccessException(UnAuthorizedAccessException e) {
        CommonResult commonResult=responseService.getFailResult(CommonResponse.UNAUTHORIZEDACCESS);
        return new ResponseEntity<>(commonResult,HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(InvaildUploadException.class)
    public ResponseEntity<CommonResult> invaildUploadException(InvaildUploadException e) {
        CommonResult commonResult=responseService.getFailResult(CommonResponse.INVALIDUPLOAD);
        return new ResponseEntity<>(commonResult,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(InvaildDataException.class)
    public ResponseEntity<CommonResult> invaildDataException(InvaildDataException e) {
        CommonResult commonResult=responseService.getFailResult(CommonResponse.INVALIDDATA);
        return new ResponseEntity<>(commonResult,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(InvaildFileExtensionException.class)
    public ResponseEntity<CommonResult> invaildFileExtensionException(InvaildFileExtensionException e) {
        CommonResult commonResult=responseService.getFailResult(CommonResponse.INVALIDFILEEXTENSION);
        return new ResponseEntity<>(commonResult,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(IntenalServerErrorException.class)
    public ResponseEntity<CommonResult> intenalServerErrorException(IntenalServerErrorException e) {
        CommonResult commonResult=responseService.getFailResult(CommonResponse.INTERNALSERVERERROR);
        return new ResponseEntity<>(commonResult,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(UnAuthorizedTokenException.class)
    public ResponseEntity<CommonResult> unAuthorizedTokenException(UnAuthorizedTokenException e) {
        CommonResult commonResult=responseService.getFailResult(CommonResponse.UNAUTHORIZEDTOKEN);
        return new ResponseEntity<>(commonResult,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NotFoundTokenException.class)
    public ResponseEntity<CommonResult> unAuthorizedTokenException(NotFoundTokenException e) {
        CommonResult commonResult=responseService.getFailResult(CommonResponse.NOTFOUNDTOKEN);
        return new ResponseEntity<>(commonResult,HttpStatus.UNAUTHORIZED);
    }
}

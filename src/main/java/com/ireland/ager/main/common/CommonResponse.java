package com.ireland.ager.main.common;

import com.ireland.ager.product.exception.InvaildProductDetailException;
import lombok.AllArgsConstructor;
import lombok.Getter;

// enum으로 api 요청 결과에 대한 code, message를 정의합니다.
@Getter
@AllArgsConstructor
public enum CommonResponse {
    SUCCESS(0, "성공하였습니다."),
    FAIL(-1, "실패하였습니다."),
    EXPIREDACCESSTOKEN(-1000,"Access Token이 만료되었습니다."),
    NOTFOUND(-2000,"존재 하지 않는 데이터입니다."),
    UNAUTHORIZEDACCESS(-1100,"권한이 없습니다."),
    INVALIDUPLOAD(-3000,"사진 파일이 없습니다."),
    INVALIDDATA(-3100,"입력 값이 잘못되었습니다."),
    INVALIDFILEEXTENSION(-3200,"확장자가 잘못되었습니다."),
    INTERNALSERVERERROR(-500,"서버 에러"),
    UNAUTHORIZEDTOKEN(-400,"카카오 액세스 토큰이 유효하지 않습니다"),
    UNAUTHORIZEDCHAT(-600,"채팅방을 개설할 수 없습니다"),
    NOTFOUNDTOKEN(-401,"카카오 액서스 토큰이 없습니다.");


    int code;
    String msg;
}
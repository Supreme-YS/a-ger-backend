package com.ireland.ager.main.common;

import com.ireland.ager.product.exception.InvaildProductCategoryException;
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
    NOTFOUNDTOKEN(-401,"카카오 액서스 토큰이 없습니다."),
    InvaildProductDetail(-3030,"상품 설명을 입력해주세요"),
    InvaildProductPrice(-3020,"상품 가격을 확인해주세요.(공백X,판매가격 0원 이상)"),
    InvaildProductTitle(-3010,"상품 제목을 입력해주세요"),
    InvaildForm(-3030,"입력값이 하나도 없습니다."),
    InvaildProductCategory(-3040,"카테고리값이 없습니다."),
    InvaildProductStatus(-3050,"상품 상태값이 없습니다."),
    DuplicateReview(-4000,"이미 리뷰를 작성하셨습니다.");




    int code;
    String msg;
}
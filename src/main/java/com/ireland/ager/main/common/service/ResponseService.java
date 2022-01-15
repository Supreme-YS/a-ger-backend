package com.ireland.ager.main.common.service;

import com.ireland.ager.main.common.CommonResponse;
import com.ireland.ager.main.common.CommonResult;
import com.ireland.ager.main.common.ListResult;
import com.ireland.ager.main.common.SingleResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseService {

    // 단일건 결과를 처리하는 메소드
    public <T> SingleResult<T> getSingleResult(T data) {
        SingleResult<T> result = new SingleResult<>();
        result.setData(data);
        setSuccessResult(result);
        return result;
    }
    //복수건 결과 처리 메소드
    public <T> ListResult<T> getListResult(List<T> list) {
        ListResult<T> result = new ListResult<>();
        result.setData(list);
        setSuccessResult(result);
        return result;
    }
    // 성공 결과만 처리하는 메소드
    public CommonResult getSuccessResult() {
        CommonResult result = new CommonResult();
        setSuccessResult(result);
        return result;
    }
    public CommonResult getFailResult(CommonResponse commonResponse) {
        CommonResult result = new CommonResult();
        setFailResult(commonResponse, result);
        return result;
    }
    private void setFailResult(CommonResponse commonResponse, CommonResult result) {
        result.setSuccess(false);
        result.setMsg(commonResponse.getMsg());
        result.setCode(commonResponse.getCode());
    }

    // 결과 모델에 api 요청 성공 데이터를 세팅해주는 메소드
    private void setSuccessResult(CommonResult result) {
        result.setSuccess(true);
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMsg(CommonResponse.SUCCESS.getMsg());
    }
}
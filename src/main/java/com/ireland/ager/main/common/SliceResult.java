package com.ireland.ager.main.common;


import lombok.Data;
import org.springframework.data.domain.Slice;

@Data
public class SliceResult<T> extends CommonResult {
    private Slice<T> data;
}
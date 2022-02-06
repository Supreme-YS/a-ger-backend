package com.ireland.ager.main.common;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Slice;

import java.util.List;

@Getter
@Setter
public class SliceResult<T> extends CommonResult {
    private Slice<T> data;
}
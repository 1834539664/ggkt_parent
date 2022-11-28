package com.atguigu.ggkt.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author WH
 * @version 1.0
 * @date 2022/11/23 16:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GgktException extends RuntimeException{

    private Integer code;

    private String msg;
}

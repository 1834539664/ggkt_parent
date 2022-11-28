package com.atguigu.ggkt.exception;

import com.atguigu.ggkt.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author WH
 * @version 1.0
 * @date 2022/11/23 16:13
 */
@ControllerAdvice  //底层使用aop,该注解用于异常处理类
public class GlobalExceptionHandler {

    //全局异常处理
    @ExceptionHandler(Exception.class)  //所有异常的处理方法
    @ResponseBody //将结果转为json返回
    public Result error(Exception e){
        e.printStackTrace();
        return Result.fail(null).message("执行了全局异常处理");
    }

    //特定异常处理
    @ExceptionHandler(ArithmeticException.class) //特定异常处理
    @ResponseBody //将结果转为json返回
    public Result error(ArithmeticException e){
        e.printStackTrace();
        return Result.fail(null).message("执行了ArithmeticException异常处理");
    }

    //自定义异常处理
    @ExceptionHandler(GgktException.class)  //所有异常的处理方法
    @ResponseBody //将结果转为json返回
    public Result error(GgktException e){
        e.printStackTrace();
        return Result.fail(null).message(e.getMsg()).code(e.getCode());
    }
}

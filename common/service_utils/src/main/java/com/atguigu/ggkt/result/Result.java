package com.atguigu.ggkt.result;


import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author WH
 * @version 1.0
 * @date 2022/11/22 22:56
 */
//统一返回结果类
@Data
@NoArgsConstructor
public  class Result<T> {
    private Integer code; //状态码

    private String message; //信息

    private T data; //返回数据

    //成功的方法,有data数据
    public static<T> Result<T> ok(T data){
        Result<T> result = new Result<>();
        result.setCode(20000);
        result.setMessage("成功");
        if (data!=null){
            result.setData(data);
        }
        return result;
    }


    //失败的方法,有data数据
    public static <T> Result<T> fail(T data){
        Result<T> result = new Result<>();
        result.setCode(20001);
        result.setMessage("失败");
        if (data!=null){
            result.setData(data);
        }
        return result;
    }

    //传入一boolean值,如果是true执行ok,false就执行fail
    public static <T> Result<T> judge(boolean isSuccess,T data){
        if (isSuccess){
            return Result.ok(data);
        }else {
            return Result.fail(data);
        }
    }

    //修改message
    public Result<T> message(String msg){
        this.setMessage(msg);
        return this;
    }

    //修改code
    public Result<T> code(Integer code){
        this.setCode(code);
        return this;
    }

    ////成功的方法,没有data数据
    //public static<T> Result<T> ok(){
    //    Result<T> result = new Result<>();
    //    result.setCode(200);
    //    result.setMessage("成功");
    //    return result;
    //}
    //
    ////失败的方法
    //public static <T> Result<T> fail(){
    //    Result<T> result = new Result<>();
    //    result.setCode(201;
    //    result.setMessage("失败");
    //    return result;
    //}
}

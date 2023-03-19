package com.atguigu.common.result;

import lombok.Data;

/**
 * @author l moonlight
 * @create 2023-03-18 20:22
 */
@Data
public class Result<T> {
    //返回码
    private Integer code;

    //返回消息
    private String message;

    //返回数据
    private T data;

    //私有化
    private Result(){}

    //封装返回时数据
    public static<T> Result<T> build(T body,ResultCodeEnum resultCodeEnum){
        Result<T> result = new Result<>();
        //封装数据
        if (body !=null){
            result.setData(body);
        }
        //状态码
        result.setCode(resultCodeEnum.getCode());
        result.setMessage(resultCodeEnum.getMessage());
        return  result;
    }

    //成功
    public static<T> Result<T> ok(){
        return build(null,ResultCodeEnum.SUCCESS);
    }

    public static<T> Result<T> ok(T data){
        return build(data,ResultCodeEnum.SUCCESS);
    }

    //失败
    public static<T>  Result<T> fail(){
        return build(null,ResultCodeEnum.FAIL);
    }

    public static<T>  Result<T> fail(T data){
        return build(data,ResultCodeEnum.FAIL);
    }

    public Result<T> message(String msg){
        this.setMessage(msg);
        return this;
    }

    public Result<T> code(Integer code){
        this.setCode(code);
        return this;
    }


}

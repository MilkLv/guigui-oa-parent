package com.atguigu.common.config.exception;

import com.atguigu.common.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author l moonlight
 * @create 2023-03-18 22:14
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 全局异常处理，执行的方法
     * 为返回json数据，加上@ResponseBody
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(){
        return Result.fail().message("执行全局异常.....");
    }

    /**
     * 特定异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public Result error(ArithmeticException e){
        //输出异常
        e.printStackTrace();
        return Result.fail().message("执行特定异常.....");
    }


    /**
     * 自定义异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(XiaoException.class)
    @ResponseBody
    public Result error(XiaoException e){
        //输出异常
        e.printStackTrace();
        return Result.fail().code(e.getCode()).message(e.getMsg());
    }

}

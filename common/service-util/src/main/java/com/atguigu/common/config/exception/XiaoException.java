package com.atguigu.common.config.exception;

import com.atguigu.common.result.ResultCodeEnum;
import lombok.Data;
import lombok.Getter;
import org.springframework.web.bind.annotation.DeleteMapping;

/**
 * @author l moonlight
 * @create 2023-03-18 22:24
 */
@Data
public class XiaoException extends RuntimeException{

    /**
     * 状态吗
     */
    private Integer code;
    /**
     * 扫描信息
     */
    private String msg;

    public XiaoException(Integer code,String msg){
        super(msg);
        this.code=code;
        this.msg=msg;
    }

    /**
     * 接收枚举类型对象
     * @param resultCodeEnum
     */
    public XiaoException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
        this.msg = resultCodeEnum.getMessage();
    }


    @Override
    public String toString() {
        return "GuliException{" +
                "code=" + code +
                ", message=" + this.getMessage() +
                '}';
    }
}

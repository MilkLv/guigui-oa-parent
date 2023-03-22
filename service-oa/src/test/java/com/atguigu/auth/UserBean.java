package com.atguigu.auth;

import org.springframework.stereotype.Component;

/**
 * @author l moonlight
 * @create 2023-03-21 21:53
 */
@Component
public class UserBean {

    public String getUsername(int id){
        if (id == 1 ){
            return "xiaoli";
        }
        if (id == 2){
            return  "jinitaimei";
        }
        return "admin";
    }
}

package com.atguigu.auth.controller;

import com.atguigu.common.result.Result;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @author l moonlight
 * @create 2023-03-19 11:01
 */
@Api(tags = "后台登录管理")
@RestController
@RequestMapping("/admin/system/index")
public class IndexControlller {

    /**
     * login
     * @return
     */
    @PostMapping("login")
    public Result login(){
        //{"code":20000,"data":{"token":"admin-token"}}
        HashMap<String, Object> map = new HashMap<>();
        map.put("token","admin-token");
        return Result.ok(map);
    }

    //{"code":20000,"data":{"roles":["admin"],"introduction":"
    // I am a super administrator","avatar":"https://wpimg.wallstcn.co
    // m/f778738c-e4f8-4870-b634-56703b4acafe.gif","name":"Super Admin"}}
    @GetMapping("info")
    public Result info(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("roles","[admin]");
        map.put("name","admin");
        map.put("avatar","https://oss.aliyuncs.com/aliyun_id_photo_bucket/default_handsome.jpg");
        return Result.ok(map);
    }

    @PostMapping("logout")
    public Result logout(){
        return Result.ok();
    }

}

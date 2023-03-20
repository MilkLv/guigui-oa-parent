package com.atguigu.auth.controller;

import com.atguigu.auth.service.SysMenuService;
import com.atguigu.auth.service.SysUserService;
import com.atguigu.common.config.exception.XiaoException;
import com.atguigu.common.jwt.JwtHelper;
import com.atguigu.common.result.Result;
import com.atguigu.common.utils.MD5;
import com.atguigu.model.system.SysUser;
import com.atguigu.vo.system.LoginVo;
import com.atguigu.vo.system.RouterVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

/**
 * @author l moonlight
 * @create 2023-03-19 11:01
 */
@Api(tags = "后台登录管理")
@RestController
@RequestMapping("/admin/system/index")
public class IndexControlller {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysMenuService sysMenuService;

    /**
     * login
     * @return
     */
    @PostMapping("login")
    public Result login(@RequestBody  LoginVo loginVo){
        //{"code":20000,"data":{"token":"admin-token"}}
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("token","admin-token");
//        return Result.ok(map);
        //1.获取输入用户名和密码
        //2根据用户名密码查询库
        String username = loginVo.getUsername();
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername,username);
        SysUser sysUser = sysUserService.getOne(wrapper);
        //用户是否存在
        if (sysUser == null){
            throw new XiaoException(201,"用户不存在");
        }
        //判断密码
        //数据库存密码（MD5）
        String passwordDb= sysUser.getPassword();
        //获取输入密码
        String passwordInput = loginVo.getPassword();
        String passwordInputMd5 = MD5.encrypt(passwordInput);
        if (!passwordDb.equals(passwordInputMd5)){
            throw new XiaoException(201,"输入密码错误");
        }
        //判断用户是否被禁用 1:可用 0：禁用
        if (sysUser.getStatus().intValue()==0){
            throw new XiaoException(201,"已被禁用，请联系管理员");
        }
        //使用jwt根据用户id和用户名称生产token字符串
        String token = JwtHelper.createToken(sysUser.getId(), sysUser.getUsername());
        //返回
        HashMap<String, Object> map = new HashMap<>();
        map.put("token",token);
        return Result.ok(map);
    }

    //{"code":20000,"data":{"roles":["admin"],"introduction":"
    // I am a super administrator","avatar":"https://wpimg.wallstcn.co
    // m/f778738c-e4f8-4870-b634-56703b4acafe.gif","name":"Super Admin"}}
    @GetMapping("info")
    public Result info(HttpServletRequest request){
        //1.从请求头中获取用户信息（获取请求头token字符串）
        String token = request.getHeader("token");
        //2.从token字符串获取用户id或着用户名称
        Long userId = JwtHelper.getUserId(token);
        //3.根据用户id查询数据库，把用户信息取出来
        SysUser sysUser = sysUserService.getById(userId);
        //4.根据用户id获取用户可以操作的菜单
        //查询数据库动态构建路由结构，进行显示
        List<RouterVo> routerList =sysMenuService.findUserMenuListByUserId(userId);
        //5.根据id获取用户可以操作按钮菜单
        List<String> permsList= sysMenuService.findUserPermsByUserId(userId);


        HashMap<String, Object> map = new HashMap<>();
        map.put("roles","[admin]");
        map.put("name",sysUser.getName());
        map.put("avatar","https://oss.aliyuncs.com/aliyun_id_photo_bucket/default_handsome.jpg");
        //返回用户可以操作的菜单
        map.put("routers",routerList);
        //用户可以操作的按钮
        map.put("buttons",permsList);
        return Result.ok(map);
    }

    @PostMapping("logout")
    public Result logout(){
        return Result.ok();
    }

}

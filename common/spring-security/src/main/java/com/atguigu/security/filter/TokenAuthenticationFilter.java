package com.atguigu.security.filter;

/**
 * @author l moonlight
 * @create 2023-03-21 1:24
 */

import com.alibaba.fastjson.JSON;
import com.atguigu.common.jwt.JwtHelper;
import com.atguigu.common.result.ResponseUtil;
import com.atguigu.common.result.Result;
import com.atguigu.common.result.ResultCodeEnum;
import com.atguigu.security.custom.LoginUserInfoHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 认证解析token过滤器
 * </p>
 */
@Slf4j
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private RedisTemplate redisTemplate;

    public TokenAuthenticationFilter(RedisTemplate redisTemplate) {
        this.redisTemplate=redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws IOException, ServletException {
        logger.info("uri:"+request.getRequestURI());
        //如果是登录接口，直接放行
        if("/admin/system/index/login".equals(request.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        if(null != authentication) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } else {
            ResponseUtil.out(response, Result.build(null, ResultCodeEnum.PERMISSION));
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        // token置于header里
        String token = request.getHeader("token");
        logger.info("token:"+token);
        if (!StringUtils.isEmpty(token)) {
            String useruame = JwtHelper.getUsername(token);
            logger.info("useruame:"+useruame);
            if (!StringUtils.isEmpty(useruame)) {
                //当前用户信息放在ThreadLocal里面
                LoginUserInfoHelper.setUserId(JwtHelper.getUserId(token));
                LoginUserInfoHelper.setUsername(useruame);
                //认证成功后通过username从redis获取权限数据
                String authString = (String) redisTemplate.opsForValue().get(useruame);
                //把redis获取字符串权限数据转换要求集合List<SimpleGrantedAuthority>
                if (!StringUtils.isEmpty(authString)){
                    //
                    List<Map> mapList = JSON.parseArray(authString, Map.class);
                    log.info(mapList.toString());
                    ArrayList<SimpleGrantedAuthority> authList = new ArrayList<>();
                    for (Map map :mapList){
                        //map.get("authority")中的authority为框架封装好的，固定的
                        authList.add(new SimpleGrantedAuthority((String) map.get("authority")));
                    }
                    return new UsernamePasswordAuthenticationToken(useruame, null,authList);
                }else {
                    return new UsernamePasswordAuthenticationToken(useruame, null,new ArrayList<>());
                }
            }
        }
        return null;
    }
}

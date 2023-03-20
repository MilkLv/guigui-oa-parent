package com.atguigu.common.jwt;

import io.jsonwebtoken.*;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * @author l moonlight
 * @create 2023-03-20 16:14
 */
public class JwtHelper {
    //有效时间
    private static long tokenExpiration = 365 * 24 * 60 * 60 * 1000;
    //签名，加密密钥
    private static String tokenSignKey = "123456";
    //根据用户id和用户名称生产token字符串
    public static String createToken(Long userId, String username) {
        String token = Jwts.builder()
                .setSubject("AUTH-USER")
                //设置有效时常
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                //设置主题部分
                .claim("userId", userId)
                .claim("username", username)
                //签名部分
                .signWith(SignatureAlgorithm.HS512, tokenSignKey)
                .compressWith(CompressionCodecs.GZIP)
                .compact();
        return token;
    }

    //从生成的token字符串获取用户id
    public static Long getUserId(String token) {
        try {
            if (StringUtils.isEmpty(token)) {
                return null;
            }
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
            Claims claims = claimsJws.getBody();
            Integer userId = (Integer) claims.get("userId");
            return userId.longValue();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //  //从生成的token字符串获取用户名称
    public static String getUsername(String token) {
        try {
            if (StringUtils.isEmpty(token)) {
                return "";
            }
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
            Claims claims = claimsJws.getBody();
            return (String) claims.get("username");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        String token = JwtHelper.createToken(1L, "admin");
        System.out.println(token);
        System.out.println(JwtHelper.getUserId(token));
        System.out.println(JwtHelper.getUsername(token));
    }
}

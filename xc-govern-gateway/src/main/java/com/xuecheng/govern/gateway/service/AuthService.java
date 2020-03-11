package com.xuecheng.govern.gateway.service;

import com.netflix.discovery.converters.Auto;
import com.xuecheng.framework.utils.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class AuthService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    //从头取出jwt令牌-Authorization
    public String getJwtFromHeader(HttpServletRequest request){
        String authorization = request.getHeader("Authorization");
        if(StringUtils.isEmpty(authorization) || !authorization.startsWith("Bearer ")){
            return null;
        }
        String jwt = authorization.substring(7);
        return jwt;
    }

    //从cookie取出token
    public String getTokenFromCookie(HttpServletRequest request){
        Map<String, String> cookieMap = CookieUtil.readCookie(request, "uid");
        String access_token = cookieMap.get("uid");
        if(StringUtils.isEmpty(access_token)){
            return null;
        }
        return access_token;
    }

    //redis取出jwt令牌
    public boolean getExpire(String access_token){
        String key = "user_token:"+access_token;
        return redisTemplate.getExpire(key, TimeUnit.SECONDS)>0;
    }
}

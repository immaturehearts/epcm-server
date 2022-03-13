package com.epcm.common;

import com.epcm.enunn.StatusEnum;
import com.epcm.exception.StatusException;
import com.epcm.manager.RedisManager;
import com.epcm.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Component
public class HttpUtil {
    @Autowired
    RedisManager redisManager;

    public String getToken(HttpServletRequest httpServletRequest){
        Cookie[] cookies = httpServletRequest.getCookies();
        String token = "";
        if (cookies == null || cookies.length == 0){
            return token;
        }
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals(UserServiceImpl.REDIS_TOKEN_KEY)){
                token = cookie.getValue();
            }
        }
        return token;
    }

    public Long getUidByToken(String token){
        Long uid = null;
        try {
            uid = Long.parseLong((String) redisManager.hGet(UserServiceImpl.REDIS_TOKEN_KEY, token));
        } catch (Exception e) {
            throw new StatusException(StatusEnum.TOKEN_EXPIRE);
        }
        return uid;
    }
}

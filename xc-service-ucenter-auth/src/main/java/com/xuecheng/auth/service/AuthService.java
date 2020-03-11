package com.xuecheng.auth.service;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.client.XcServiceList;
import com.xuecheng.framework.domain.ucenter.ext.AuthToken;
import com.xuecheng.framework.domain.ucenter.response.AuthCode;
import com.xuecheng.framework.exception.ExceptionCast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class AuthService {

    @Value("${auth.tokenValiditySeconds}")
    private long tokenValiditySeconds;

    @Autowired
    LoadBalancerClient loadBalancerClient;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private StringRedisTemplate redisTemplate;

    public AuthToken login(String username, String password, String clientId, String clientSecret) {
        //请求spring security申请令牌
        AuthToken authToken = this.applyToken(username, password, clientId, clientSecret);
        if(authToken==null){
            ExceptionCast.cast(AuthCode.AUTH_LOGIN_APPLYTOKEN_FAIL);
        }
        String access_token = authToken.getAccess_token();
        String content = JSON.toJSONString(authToken);
        boolean flag = this.saveToken(access_token, content, tokenValiditySeconds);
        if(!flag){
            ExceptionCast.cast(AuthCode.AUTH_LOGIN_TOKEN_SAVEFAIL);
        }
        return authToken;
    }

    private AuthToken applyToken(String username, String password, String clientId, String clientSecret){
        ServiceInstance serviceInstance = loadBalancerClient.choose(XcServiceList.XC_SERVICE_UCENTER_AUTH);
        URI uri = serviceInstance.getUri();
        String authUrl = uri + "/auth/oauth/token";

        LinkedMultiValueMap<String,String> header = new LinkedMultiValueMap<>();
        header.add("authorization",getHttpBasic(clientId,clientSecret));

        LinkedMultiValueMap<String,String> body = new LinkedMultiValueMap<>();
        body.add("grant_type","password");
        body.add("username",username);
        body.add("password",password);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(body,header);

        restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if(response.getRawStatusCode()!=400 && response.getRawStatusCode()!=401)
                    super.handleError(response);
            }
        });

        //exchange:允许调用者指定HTTP请求的方法、可以在请求中增加body以及头信息
        ResponseEntity<Map> exchange = restTemplate.exchange(authUrl, HttpMethod.POST, httpEntity, Map.class);
        //申请的令牌的信息
        Map tokenMap = exchange.getBody();

        if(tokenMap==null ||
            tokenMap.get("access_token")==null ||
                tokenMap.get("refresh_token")==null ||
                tokenMap.get("jti")==null){
            String error_description = (String) tokenMap.get("error_description");
            if(error_description.indexOf("UserDetailsService returned null")!=-1){
                ExceptionCast.cast(AuthCode.AUTH_ACCOUNT_NOTEXISTS);
            }else if(error_description.indexOf("坏的凭证")!=-1){
                ExceptionCast.cast(AuthCode.AUTH_CREDENTIAL_ERROR);
            }

            return null;
        }
        AuthToken authToken = new AuthToken();
        authToken.setAccess_token((String)tokenMap.get("jti"));
        authToken.setRefresh_token((String)tokenMap.get("refresh_token"));
        authToken.setJwt_token((String)tokenMap.get("access_token"));
        return authToken;
    }

    //存储令牌到redis

    /**
     *
     * @param access_token jwi用户身份令牌
     * @param content  authToken对象内容
     * @param expire  有效时间
     * @return
     */
    private boolean saveToken(String access_token,String content, long expire){
        String key = "user_token:" + access_token;
        redisTemplate.boundValueOps(key).set(content,expire, TimeUnit.SECONDS);
        Long flag = redisTemplate.getExpire(key,TimeUnit.SECONDS);
        return flag > 0;
    }

    private String getHttpBasic(String clientId,String clientSecret){
        String s = clientId+":"+clientSecret;
        byte[] encode = Base64Utils.encode(s.getBytes());
        return "Basic "+new String(encode);
    }

    //从redis中查询令牌
    public AuthToken getToken(String jwi){
        String key = "user_token:" + jwi;
        String tokenJson = redisTemplate.opsForValue().get(key);
        try {
            AuthToken authToken = JSON.parseObject(tokenJson, AuthToken.class);
            return authToken;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //redis中删除令牌
    public boolean delToken(String jwi){
        String key = "user_token:" + jwi;
        redisTemplate.delete(key);
        Long expire = redisTemplate.getExpire(key, TimeUnit.SECONDS);
        //return expire < 0; // 在reids中可能过期了，删除失败
        return true;
    }
}

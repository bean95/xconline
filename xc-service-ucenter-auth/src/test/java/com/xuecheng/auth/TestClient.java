package com.xuecheng.auth;

import com.xuecheng.framework.client.XcServiceList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestClient {

    @Autowired
    LoadBalancerClient loadBalancerClient;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void testClient(){

        ServiceInstance serviceInstance = loadBalancerClient.choose(XcServiceList.XC_SERVICE_UCENTER_AUTH);
        URI uri = serviceInstance.getUri();
        String authUrl = uri + "/auth/oauth/token";

        LinkedMultiValueMap<String,String> header = new LinkedMultiValueMap<>();
        header.add("authorization",getHttpBasic("XcWebApp","XcWebApp"));

        LinkedMultiValueMap<String,String> body = new LinkedMultiValueMap<>();
        body.add("grant_type","password");
        body.add("username","itcast");
        body.add("password","1234");

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(body,header);

        restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if(response.getRawStatusCode()!=400 && response.getRawStatusCode()!=401)
                    super.handleError(response);
            }
        });

        ResponseEntity<Map> exchange = restTemplate.exchange(authUrl, HttpMethod.POST, httpEntity, Map.class);
        //申请的令牌的信息
        Map tokenMap = exchange.getBody();

        System.out.println(tokenMap);
        //redirect_uri   http://localhost

    }

    private String getHttpBasic(String clientId,String clientSecret){
        String s = clientId+":"+clientSecret;
        byte[] encode = Base64Utils.encode(s.getBytes());
        return "Basic "+new String(encode);
    }

}

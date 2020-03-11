package com.xuecheng.govern.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.govern.gateway.service.AuthService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginFilter extends ZuulFilter {

    @Autowired
    private AuthService authService;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {

        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        HttpServletResponse response = requestContext.getResponse();

        //cookie
        String tokenFromCookie = authService.getTokenFromCookie(request);
        String jwtFromHeader = authService.getJwtFromHeader(request);
        boolean expire = authService.getExpire(tokenFromCookie);
        if(StringUtils.isEmpty(tokenFromCookie)||StringUtils.isEmpty(jwtFromHeader)||!expire){
            access_denied();
            return null;
        }
        return null;
    }

    //拒绝访问
    private void access_denied(){
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletResponse response = requestContext.getResponse();
        requestContext.setSendZuulResponse(false);//不再向下请求转发
        requestContext.setResponseStatusCode(200);
        ResponseResult result = new ResponseResult(CommonCode.UNAUTHENTICATED);
        String json = JSON.toJSONString(result);
        requestContext.setResponseBody(json);
        response.setContentType("application/json;charset=utf-8");
    }
}

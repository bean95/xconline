package com.xuecheng.framework.exception;

import com.google.common.collect.ImmutableMap;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice //控制器增强器
public class ExceptionCatch {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionCatch.class);

    //定义map
    private static ImmutableMap<Class<? extends Throwable>,ResultCode> EXCEPTIONS;
    protected static ImmutableMap.Builder<Class<? extends Throwable>,ResultCode> builder = ImmutableMap.builder();


    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResponseResult customException(CustomException ex){
        LOGGER.error("catch exception:{}",ex.getMessage());
        ResultCode resultCode = ex.getResultCode();
        return new ResponseResult(resultCode);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult exception(Exception ex){
        LOGGER.error("catch exception:{}",ex.getMessage());
        if(EXCEPTIONS == null){
            EXCEPTIONS = builder.build();
        }
        ResultCode resultCode = EXCEPTIONS.get(ex.getClass());
        if(resultCode != null)
            return new ResponseResult(resultCode);
        return new ResponseResult(CommonCode.SERVER_ERROR);
    }

    static{
        builder.put(HttpMessageNotReadableException.class, CommonCode.INVALID_PARAM);
    }

}

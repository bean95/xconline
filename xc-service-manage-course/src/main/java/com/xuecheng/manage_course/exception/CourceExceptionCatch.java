package com.xuecheng.manage_course.exception;

import com.xuecheng.framework.exception.ExceptionCatch;
import com.xuecheng.framework.model.response.CommonCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * 课程管理中自定义的异常类
 */
@ControllerAdvice
public class CourceExceptionCatch extends ExceptionCatch {


    static{
        builder.put(AccessDeniedException.class, CommonCode.UNAUTHORISE);
    }
}

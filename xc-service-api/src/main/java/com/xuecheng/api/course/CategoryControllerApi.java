package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "course category management api",description = "增删改查")
public interface CategoryControllerApi {

    @ApiOperation("课程类别查询")
    CategoryNode findList();
}

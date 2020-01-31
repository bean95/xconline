package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.system.SysDictionary;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "数据字典",description = "数据字典查询接口")
public interface SysDictionaryControllerApi {

    @ApiOperation("更加类型查询数据字典")
    SysDictionary getByType(String type);
}

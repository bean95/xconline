package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@Api(value="cms页面管理接口",description = "cms页面管理接口，提供页面的增、删、改、查")
public class CmsPageService {

    @Autowired
    private CmsPageRepository cmsPageRepository;

    @ApiOperation("分页查询页面列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="page",value = "页码",required=true,paramType="path",dataType="int"),
            @ApiImplicitParam(name="size",value = "每页记录数",required=true,paramType="path",dataType="int")
                    })
    public QueryResponseResult findList(int page,int size, QueryPageRequest queryPageRequest){

        if(page <= 0){
            page = 1;
        }
        if(size <= 0){
            size = 10;
        }

        //Example
        CmsPage cmsPage = new CmsPage();
        BeanUtils.copyProperties(queryPageRequest,cmsPage);
        Example<CmsPage> example = Example.of(cmsPage, ExampleMatcher.matching().withMatcher("pageAliase",ExampleMatcher.GenericPropertyMatchers.contains()));


        Pageable pageable = PageRequest.of(page-1,size);
        Page<CmsPage> all = cmsPageRepository.findAll(example,pageable);

        QueryResult<CmsPage> queryResult = new QueryResult<>();
        queryResult.setList(all.getContent());
        queryResult.setTotal(all.getTotalElements());
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,queryResult);
        return queryResponseResult;

    }
}

package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        //BeanUtils.copyProperties(queryPageRequest,cmsPage);
        if(StringUtils.isNotEmpty(queryPageRequest.getSiteId()))
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        if(StringUtils.isNotEmpty(queryPageRequest.getPageAliase()))
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        Example<CmsPage> example = Example.of(cmsPage, ExampleMatcher.matching().withMatcher("pageAliase",ExampleMatcher.GenericPropertyMatchers.contains()));


        Pageable pageable = PageRequest.of(page-1,size);
        Page<CmsPage> all = cmsPageRepository.findAll(example,pageable);

        QueryResult<CmsPage> queryResult = new QueryResult<>();
        queryResult.setList(all.getContent());
        queryResult.setTotal(all.getTotalElements());
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,queryResult);
        return queryResponseResult;

    }

    public CmsPageResult add(CmsPage cmsPage){
        //校验页面名称、站点Id、页面webpath的唯一性 ---唯一索引
        CmsPage existPage = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(),cmsPage.getSiteId(),cmsPage.getPageWebPath());
        if(existPage == null){
            cmsPage.setPageId(null);
            cmsPageRepository.save(cmsPage);
            return new CmsPageResult(CommonCode.SUCCESS,cmsPage);
        }
        return new CmsPageResult(CommonCode.FAIL,null);
    }

    public CmsPage getById(String id){
        final Optional<CmsPage> optional = cmsPageRepository.findById(id);
        if(optional.isPresent()){
            return optional.get();
        }
        return null;
    }

    public CmsPageResult update(String id, CmsPage cmsPage){
        CmsPage origin = this.getById(id);
        if(origin != null){
            origin.setTemplateId(cmsPage.getTemplateId());
            origin.setSiteId(cmsPage.getSiteId());
            origin.setPageAliase(cmsPage.getPageAliase());
            origin.setPageName(cmsPage.getPageName());
            origin.setPageWebPath(cmsPage.getPageWebPath());
            origin.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
            origin.setPageType(cmsPage.getPageType());
            cmsPageRepository.save(origin);
            return new CmsPageResult(CommonCode.SUCCESS,origin);
        }
        return new CmsPageResult(CommonCode.FAIL,null);
    }

    public ResponseResult delete(String id){
        CmsPage origin = this.getById(id);
        if(origin != null){
            cmsPageRepository.deleteById(id);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

}

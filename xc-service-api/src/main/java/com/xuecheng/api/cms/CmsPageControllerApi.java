package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;

@Api(value = "cms服务")
public interface CmsPageControllerApi {

    QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);
    CmsPageResult add(CmsPage cmsPage);
    CmsPage findById(String id);
    CmsPageResult edit(String id,CmsPage cmsPage);
    ResponseResult delete(String id);
    ResponseResult post(String pageId);

    //供课程管理服务远程调用
    CmsPageResult save(CmsPage cmsPage);
    //一键发布
    CmsPostPageResult postPageQuick(CmsPage cmsPage);


}

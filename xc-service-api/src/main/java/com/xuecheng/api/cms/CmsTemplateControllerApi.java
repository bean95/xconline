package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryTemplateRequest;
import com.xuecheng.framework.domain.cms.response.CmsTemplateResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CmsTemplateControllerApi {

    QueryResponseResult findList(int page, int size, QueryTemplateRequest queryTemplateRequest);
    QueryResponseResult findAll();
    //CmsTemplateResult add(CmsTemplate cmsTemplate);
    CmsTemplate findById(String id);
    //CmsTemplateResult edit(String id,CmsTemplate cmsTemplate);
    ResponseResult delete(String id);
    CmsTemplateResult add(CmsTemplate cmsTemplate, MultipartFile file) throws IOException;
    CmsTemplateResult edit(String id,CmsTemplate cmsTemplate,MultipartFile file) throws IOException;

}

package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;

public interface CmsPageControllerApi {

    QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) ;
}

package com.xuecheng.framework.domain.cms.request;

import lombok.Data;

@Data
public class QueryTemplateRequest {

    //站点ID
    private String siteId;
    //模版名称
    private String templateName;
    //模版参数
    private String templateParameter;
}

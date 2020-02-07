package com.xuecheng.manage_course.feigninterface;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "XC-SERVICE-MANAGE-CMS")  //指定远程调用的服务名
public interface CmsPageClient {

    //http://localhost:31001/cms/page/get/5a795ac7dd573c04508f3a56
    @GetMapping("/cms/page/get/{id}")
    public CmsPage findById(@PathVariable("id") String id);
}

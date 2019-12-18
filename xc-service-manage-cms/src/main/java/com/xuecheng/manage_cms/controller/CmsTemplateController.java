package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsTemplateControllerApi;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryTemplateRequest;
import com.xuecheng.framework.domain.cms.response.CmsTemplateResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.service.CmsTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cms/template")
public class CmsTemplateController implements CmsTemplateControllerApi {

    @Autowired
    private CmsTemplateService cmsTemplateService;

    @Override
    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult findList(@PathVariable("page") int page, @PathVariable("size") int size, QueryTemplateRequest queryTemplateRequest) {
        return cmsTemplateService.findList(page,size,queryTemplateRequest);
    }

    @Override
    @GetMapping("list")
    public QueryResponseResult findAll() {
        return cmsTemplateService.findAll();
    }

    @Override
    @PostMapping("add")
    public CmsTemplateResult add(@RequestBody CmsTemplate cmsTemplate) {
        return cmsTemplateService.add(cmsTemplate);
    }

    @Override
    @GetMapping("/get/{id}")
    public CmsTemplate findById(@PathVariable("id")String id) {
        return cmsTemplateService.findById(id);
    }

    @Override
    @PutMapping("/edit/{id}")
    public CmsTemplateResult edit(@PathVariable("id") String id,@RequestBody CmsTemplate cmsTemplate) {
        return cmsTemplateService.update(id,cmsTemplate);
    }

    @Override
    @DeleteMapping("/del/{id}")
    public ResponseResult delete(@PathVariable("id") String id) {
        return cmsTemplateService.delete(id);
    }
}

package com.xuecheng.manage_cms.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryTemplateRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsTemplateResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
public class CmsTemplateService {

    @Autowired
    private CmsTemplateRepository cmsTemplateRepository;
    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    private GridFSBucket gridFSBucket;

    public QueryResponseResult findList(int page, int size, QueryTemplateRequest queryTemplateRequest){

        if(page <= 0){
            page = 1;
        }
        if(size <= 0){
            size = 10;
        }
        CmsTemplate cmsTemplate = new CmsTemplate();
        if(StringUtils.isNotEmpty(queryTemplateRequest.getSiteId())){
            cmsTemplate.setSiteId(queryTemplateRequest.getSiteId());
        }
        if(StringUtils.isNotEmpty(queryTemplateRequest.getTemplateName())){
            cmsTemplate.setTemplateName(queryTemplateRequest.getTemplateName());
        }
        if(StringUtils.isNotEmpty(queryTemplateRequest.getTemplateParameter())){
            cmsTemplate.setTemplateParameter(queryTemplateRequest.getTemplateParameter());
        }

        Example<CmsTemplate> example = Example.of(cmsTemplate, ExampleMatcher.matching().withMatcher("templateName",ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("templateParameter",ExampleMatcher.GenericPropertyMatchers.contains()));
        Pageable pageable = PageRequest.of(page-1,size);

        Page<CmsTemplate> all = cmsTemplateRepository.findAll(example, pageable);

        QueryResult<CmsTemplate> queryResult = new QueryResult();
        queryResult.setList(all.getContent());
        queryResult.setTotal(all.getTotalElements());

        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,queryResult);
        return queryResponseResult;

    }

    public QueryResponseResult findAll(){

        List<CmsTemplate> all = cmsTemplateRepository.findAll();
        QueryResult<CmsTemplate> queryResult = new QueryResult<>();
        queryResult.setList(all);
        queryResult.setTotal(all.size());
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,queryResult);
        return queryResponseResult;

    }

    /*public CmsTemplateResult add(CmsTemplate cmsTemplate){

        CmsTemplate existTemplate = cmsTemplateRepository.findBySiteIdAndTemplateNameAndTemplateParameter(cmsTemplate.getSiteId(), cmsTemplate.getTemplateName(), cmsTemplate.getTemplateParameter());
        if(existTemplate != null){
            ExceptionCast.cast(CmsCode.CMS_TEMPLATE_ISEXIST);
        }
        cmsTemplate.setTemplateId(null);
        cmsTemplateRepository.save(cmsTemplate);
        return new CmsTemplateResult(CommonCode.SUCCESS,cmsTemplate);
    }*/

    public CmsTemplate findById(String id){
        Optional<CmsTemplate> optional = cmsTemplateRepository.findById(id);
        if(optional.isPresent()){
            return optional.get();
        }
        return null;
    }

    /*public CmsTemplateResult update(String id,CmsTemplate cmsTemplate){
        CmsTemplate origin = findById(id);
        if(origin != null){
            origin.setTemplateParameter(cmsTemplate.getTemplateParameter());
            origin.setTemplateName(cmsTemplate.getTemplateName());
            origin.setSiteId(cmsTemplate.getSiteId());
            origin.setTemplateFileId(cmsTemplate.getTemplateFileId());
            cmsTemplateRepository.save(origin);
            return new CmsTemplateResult(CommonCode.SUCCESS,origin);
        }
        return new CmsTemplateResult(CommonCode.FAIL,null);
    }*/

    public ResponseResult delete(String id){
        CmsTemplate origin = findById(id);
        if(origin != null){
            //删除原模板
            if(StringUtils.isNotEmpty(origin.getTemplateFileId())){
                removeOldTemplate(origin.getTemplateFileId());
            }
            cmsTemplateRepository.deleteById(id);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    //上传模板文件
    public ObjectId uploadTemplat(InputStream inputStream,String fileName){
        ObjectId objectId = gridFsTemplate.store(inputStream, fileName);
       return objectId;
    }
    //删除原模板文件
    public void removeOldTemplate(String templateFileId){
        gridFsTemplate.delete(Query.query(Criteria.where("_id").is(templateFileId)));
    }


    public CmsTemplateResult add(CmsTemplate cmsTemplate, MultipartFile file) throws IOException {
        CmsTemplate existTemplate = cmsTemplateRepository.findBySiteIdAndTemplateNameAndTemplateParameter(cmsTemplate.getSiteId(), cmsTemplate.getTemplateName(), cmsTemplate.getTemplateParameter());
        if(existTemplate != null){
            ExceptionCast.cast(CmsCode.CMS_TEMPLATE_ISEXIST);
        }
        if(file != null){
            ObjectId fileId = uploadTemplat(file.getInputStream(),file.getOriginalFilename());
            cmsTemplate.setTemplateFileId(fileId.toString());
        }
        cmsTemplate.setTemplateId(null);
        cmsTemplateRepository.save(cmsTemplate);
        return new CmsTemplateResult(CommonCode.SUCCESS,cmsTemplate);
    }

    public CmsTemplateResult update(String id,CmsTemplate cmsTemplate,MultipartFile file) throws IOException {
        CmsTemplate origin = findById(id);
        if(origin != null){
            origin.setTemplateParameter(cmsTemplate.getTemplateParameter());
            origin.setTemplateName(cmsTemplate.getTemplateName());
            origin.setSiteId(cmsTemplate.getSiteId());
            if(file != null){

                //删除原模板
                if(StringUtils.isNotEmpty(origin.getTemplateFileId())){
                    removeOldTemplate(origin.getTemplateFileId());
                }
                ObjectId fileId = uploadTemplat(file.getInputStream(),file.getOriginalFilename());
                origin.setTemplateFileId(fileId.toString());
            }
            cmsTemplateRepository.save(origin);
            return new CmsTemplateResult(CommonCode.SUCCESS,origin);
        }
        return new CmsTemplateResult(CommonCode.FAIL,null);
    }
}

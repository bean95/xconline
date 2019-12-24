package com.xuecheng.manage_cms_client.mq;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.manage_cms_client.service.CmsPageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ConsumerPostPate {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerPostPate.class);

    @Autowired
    private CmsPageService cmsPageService;

    @RabbitListener(queues = {"${xuecheng.mq.queue}"})
    public void postPate(String msg){

        Map map = JSON.parseObject(msg, Map.class);
        String pageId = (String)map.get("pageId");

        CmsPage cmsPage = cmsPageService.findCmsPageById(pageId);
        if(cmsPage == null){
            LOGGER.error("receive postpage msg,Cmspage is null,pageid:{}",pageId);
            return ;
        }

        cmsPageService.savePageToServerPath(pageId);
    }
}

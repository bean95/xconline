package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.manage_course.feigninterface.CmsPageClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RibbonTest {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CmsPageClient cmsPageClient;

    @Test
    public void testRibbon(){
        String serviceId = "XC-SERVICE-MANAGE-CMS";
        for(int i = 1;i<5;i++){
            String url = "http://"+ serviceId +"/cms/page/get/5a795ac7dd573c04508f3a56";
            ResponseEntity<Map> forEntity = restTemplate.getForEntity(url, Map.class);
            System.out.println(forEntity.getBody());
        }
    }

    @Test
    public void testFeign(){
        for(int i = 1;i<5;i++){
            CmsPage cmsPage = cmsPageClient.findById("5a795ac7dd573c04508f3a56");
            System.out.println(cmsPage);
        }
    }
}

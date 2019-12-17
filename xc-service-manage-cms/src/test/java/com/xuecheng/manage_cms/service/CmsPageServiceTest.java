package com.xuecheng.manage_cms.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageServiceTest {

    @Autowired
    private CmsPageService cmsPageService;

    @Test
    public void test(){
        String pageHtml = cmsPageService.getPageHtml("5df8fc21d7349a06f824feab");
        System.out.println(pageHtml);
    }
}

package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.beans.Beans;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageRepositoryTest {

    @Autowired
    private CmsPageRepository cmsPageRepository;

    @Test
    public void testFindAll(){
        List<CmsPage> all = cmsPageRepository.findAll();
        Assert.assertNotEquals(0,all.size());

    }

    @Test
    public void testFindPage(){
        Pageable pageable = PageRequest.of(0,5);
        Page<CmsPage> all = cmsPageRepository.findAll(pageable);
        Assert.assertNotEquals(0,all.getTotalPages());

    }
    
    @Test
    public void testFindById(){
        Optional<CmsPage> optional = cmsPageRepository.findById("5abefd525b05aa293098fca6");
        if(optional.isPresent()){
            System.out.println(optional.get().getPageName());
        }
    }

    @Test
    public void testSave(){
        CmsPage cmsPage = new CmsPage();
        cmsPage.setPageName("test1202");
        CmsPage p = cmsPageRepository.save(cmsPage);
        System.out.println(p);

    }

    @Test
    public void testUpdate(){
        Optional<CmsPage> optional = cmsPageRepository.findById("5de51bdad7349a2c64e65330");
        if(optional.isPresent()){
            CmsPage cmsPage = optional.get();
            cmsPage.setPageName("updateIndex.html");
            cmsPage.setPageAliase("更新别明");
            cmsPageRepository.save(cmsPage);
        }

    }

    @Test
    public void testFindByPageAliase(){
        CmsPage cmsPage = cmsPageRepository.findBypageAliase("更新别明");
        System.out.println(cmsPage.getPageName());
    }

    @Test
    public void testDeleteById(){
        cmsPageRepository.deleteById("5de51cd3d7349a2db8bb3f98");
    }

    @Test
    public void testDeleteByPageAliase(){
        cmsPageRepository.deleteBypageAliase("更新别明");
    }

    @Test
    public void testFindAllByExample(){

        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page,size);

        CmsPage query = new CmsPage();
        query.setPageAliase("页");

        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("pageAliase",ExampleMatcher.GenericPropertyMatchers.contains());

        Example<CmsPage> example = Example.of(query,exampleMatcher);

        Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);
        System.out.println(all.getContent());

    }

}

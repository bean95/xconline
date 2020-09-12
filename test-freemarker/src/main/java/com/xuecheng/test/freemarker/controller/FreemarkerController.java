package com.xuecheng.test.freemarker.controller;

import com.xuecheng.test.freemarker.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Controller
@RequestMapping("/freemarker")
public class FreemarkerController {

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/test1")
    public String test1(Map<String,Object> map){

        //map放在springmvc中的形参，map里的数据会放在
        //request的域响应给用户
        map.put("name","bean95");
        Student stu1 = new Student();
        stu1.setName("小明");
        stu1.setAge(18);
        stu1.setMoney(1000.86f);
        stu1.setBirthday(new Date());
        Student stu2 = new Student();
        stu2.setName("小红");
        stu2.setMoney(200.1f);
        stu2.setAge(19);
        stu2.setBirthday(new Date());
        List<Student> friends = new ArrayList<>();
        friends.add(stu1);
        stu2.setFriends(friends);
        stu2.setBestFriend(stu1);
        List<Student> stus = new ArrayList<>();
        stus.add(stu1);
        stus.add(stu2);
        map.put("stus",stus);
        HashMap<String,Student> stuMap = new HashMap<>();
        stuMap.put("stu1",stu1);
        stuMap.put("stu2",stu2);
        map.put("stu1",stu1);
        map.put("stuMap",stuMap);

        map.put("money",123456789);

        return "test1";  //默认resources/templates
    }

    //index_banner
    @RequestMapping("/banner")
    public String index_banner(Map<String,Object> map){
        String url = "http://localhost:31001/cms/config/getmodel/5a791725dd573c3574ee333f";
        ResponseEntity<Map> forEntity = restTemplate.getForEntity(url, Map.class);
        Map model = forEntity.getBody();
        map.putAll(model);
        return "index_banner";
    }

    @RequestMapping("/course")
    public String index_course(Map<String,Object> map){
        String url = "http://localhost:31200/course/courseview/4028e581617f945f01617f9dabc40000";
        ResponseEntity<Map> forEntity = restTemplate.getForEntity(url, Map.class);
        Map model = forEntity.getBody();
        map.putAll(model);
        return "course";
    }


}

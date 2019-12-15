package com.xuecheng.test.freemarker;

import com.xuecheng.test.freemarker.model.Student;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class FreemarkerTest {


    @Test
    public void genHtmlByTemplate() throws IOException, TemplateException {
        Configuration configuration = new Configuration(Configuration.getVersion());

        //定义模板   temp1.ftl
        String classPath = this.getClass().getResource("/").getPath();
        configuration.setDirectoryForTemplateLoading(new File(classPath + "/templates"));
        Template template = configuration.getTemplate("temp1.ftl");
        //定义数据模型
        Map map = getMap();
        //静态化
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);

        //apache io工具包
        InputStream in = IOUtils.toInputStream(content);
        FileOutputStream out = new FileOutputStream(new File("C:\\Users\\Administrator\\Desktop\\freemarker.html"));
        IOUtils.copy(in,out);
        in.close();
        out.close();
    }

    @Test
    public void genHtmlByString() throws IOException, TemplateException {
        Configuration configuration = new Configuration(Configuration.getVersion());

        //定义模板   temp1.ftl
        String templateStr = ""+
                "<html>\n" +
                "  <head></head>\n" +
                "   <body>\n" +
                "   名称：${name}\n" +
                "   </body>\n" +
                "</html>";

        //使用模板加载器把字符串变为模板
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate("temp2",templateStr);
        //在配置中设置模板加载器
        configuration.setTemplateLoader(stringTemplateLoader);
        //获取模板对象
        Template template = configuration.getTemplate("temp2", "utf-8");
        //定义数据模型
        Map map = getMap();
        //静态化
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);

        //apache io工具包
        InputStream in = IOUtils.toInputStream(content);
        FileOutputStream out = new FileOutputStream(new File("C:\\Users\\Administrator\\Desktop\\freemarker.html"));
        IOUtils.copy(in,out);
        in.close();
        out.close();
    }

    public Map getMap(){
        Map map = new HashMap();
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
        return map;
    }

}

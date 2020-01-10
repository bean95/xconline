package com.xuecheng.manage_course.serivce;

import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.manage_course.dao.TeachplanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseService {

    @Autowired
    TeachplanMapper teachplanMapper;

    public TeachplanNode findTeachplanList(String courseId){
        return teachplanMapper.selectList(courseId);
    }
}

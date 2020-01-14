package com.xuecheng.manage_course.serivce;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.dao.CourseBaseRepository;
import com.xuecheng.manage_course.dao.TeachplanMapper;
import com.xuecheng.manage_course.dao.TeachplanRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private TeachplanMapper teachplanMapper;
    @Autowired
    private TeachplanRepository teachplanRepository;
    @Autowired
    private CourseBaseRepository courseBaseRepository;

    public TeachplanNode findTeachplanList(String courseId){
        return teachplanMapper.selectList(courseId);
    }

    public ResponseResult addTeachplan(Teachplan teachplan){

        if(teachplan == null || StringUtils.isEmpty(teachplan.getCourseid()) || StringUtils.isEmpty(teachplan.getPname())){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        String courseId = teachplan.getCourseid();
        String parentId = teachplan.getParentid();Teachplan teachplanNew = new Teachplan();
        BeanUtils.copyProperties(teachplan,teachplanNew);
        Teachplan parentNode = this.getTeachplanRoot(courseId);
        if(StringUtils.isEmpty(parentId)){
            teachplanNew.setParentid(parentNode.getId());
        }
        if("1".equals(parentNode.getGrade())){
            teachplanNew.setGrade("2");
        }else{
            teachplanNew.setGrade("3");
        }
        teachplanRepository.save(teachplanNew);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //查询课程根节点
    @Transactional//mysql数据库支持事务控制，mongodb不支持
    public Teachplan getTeachplanRoot(String courseId){
        List<Teachplan> teachplanList = teachplanRepository.findByCourseidAndParentid(courseId, "0");
        if(teachplanList == null || teachplanList.size() ==0){

            Optional<CourseBase> optional = courseBaseRepository.findById(courseId);
            if(!optional.isPresent()){
                return null;
            }
            CourseBase courseBase = optional.get();
            Teachplan teachplan = new Teachplan();
            teachplan.setParentid("0");
            teachplan.setGrade("1");
            teachplan.setPname(courseBase.getName());
            teachplan.setCourseid(courseId);
            teachplan.setStatus("0");
            teachplanRepository.save(teachplan);
            //return teachplan.getId();
            return teachplan;
        }
        //return teachplanList.get(0).getId();
        return teachplanList.get(0);
    }
}

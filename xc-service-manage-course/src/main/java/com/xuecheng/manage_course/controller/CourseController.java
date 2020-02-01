package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CourseControllerApi;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.serivce.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/course")
public class CourseController implements CourseControllerApi {

    @Autowired
    private CourseService courseService;


    @Override
    @GetMapping("/teachplan/list/{courseId}")
    public TeachplanNode findTeachplanList(@PathVariable("courseId") String courseId) {
        return courseService.findTeachplanList(courseId);
    }

    @Override
    @PostMapping("/teachplan/add")
    public ResponseResult addTeachplan(@RequestBody Teachplan teachplan) {
        return courseService.addTeachplan(teachplan);
    }

    @Override
    @GetMapping("/coursebase/list/{page}/{size}")
    public QueryResponseResult findCourseList(@PathVariable("page") int page, @PathVariable("size") int size, CourseListRequest courseListRequest) {
        return courseService.findCourseList(page,size,courseListRequest);
    }

    @Override
    @PostMapping("/coursebase/add")
    public ResponseResult addCourseBase(@RequestBody CourseBase courseBase) {
        //System.out.println(courseBase);
        return courseService.addCourseBase(courseBase);
    }

    @Override
    @GetMapping("/coursebase/getCoursebaseById/{courseId}")
    public CourseBase getCoursebaseById(@PathVariable("courseId") String courseId) {
        return courseService.findCourseBaseById(courseId);
    }

    @Override
    @PutMapping("/coursebase/updateCoursebase/{courseId}")
    public ResponseResult updateCoursebase(@PathVariable("courseId")String courseId,@RequestBody CourseBase courseBase) {
        return courseService.updateCoursebase(courseId,courseBase);
    }


}

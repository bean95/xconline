package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "course management api",description = "增删改查")
public interface CourseControllerApi {

    @ApiOperation("课程计划查询")
    TeachplanNode findTeachplanList(String courseId);

    @ApiOperation("添加课程计划")
    ResponseResult addTeachplan(Teachplan teachplan);

    @ApiOperation("课程列表查询")
    QueryResponseResult findCourseList(int page, int size, CourseListRequest courseListRequest);

    @ApiOperation("新增课程")
    ResponseResult addCourseBase(CourseBase courseBase);

    @ApiOperation("查询特定课程信息")
    CourseBase getCoursebaseById(String courseId);

    @ApiOperation("更新课程信息")
    ResponseResult updateCoursebase(String id,CourseBase courseBase);

    @ApiOperation("添加课程图片")
    ResponseResult addCoursePic(String coursdId,String pic);

    @ApiOperation("查询课程图片")
    CoursePic findCoursePic(String coursdId);

    @ApiOperation("删除课程图片")
    ResponseResult deleteCoursePic(String coursdId);

    @ApiOperation("课程详情查询")
    CourseView courseview(String coursdId);

    @ApiOperation("课程预览")
    CoursePublishResult preview(String id);
}

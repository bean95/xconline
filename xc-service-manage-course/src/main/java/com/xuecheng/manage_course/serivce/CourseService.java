package com.xuecheng.manage_course.serivce;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.dao.*;
import com.xuecheng.manage_course.feigninterface.CmsPageClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
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
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private CoursePicRepository coursePicRepository;
    @Autowired
    private CourseMarketRepository courseMarketRepository;
    @Autowired
    private CmsPageClient cmsPageClient;
    @Autowired
    private  CoursePubRepository coursePubRepository;

    @Value("${course-publish.siteId}")
    private String siteId;
    @Value("${course-publish.templateId}")
    private String templateId;
    @Value("${course-publish.previewUrl}")
    private String previewUrl;
    @Value("${course-publish.pageWebPath}")
    private String pageWebPath;
    @Value("${course-publish.pagePhysicalPath}")
    private String pagePhysicalPath;
    @Value("${course-publish.dataUrlPre}")
    private String dataUrlPre;


    public TeachplanNode findTeachplanList(String courseId){
        return teachplanMapper.selectList(courseId);
    }

    @Transactional
    public ResponseResult addTeachplan(Teachplan teachplan){

        if(teachplan == null || StringUtils.isEmpty(teachplan.getCourseid()) || StringUtils.isEmpty(teachplan.getPname())){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        String courseId = teachplan.getCourseid();
        String parentId = teachplan.getParentid();
        Teachplan teachplanNew = new Teachplan();
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

    public QueryResponseResult findCourseList(int pageNo, int pageSize, CourseListRequest courseListRequest){
        PageHelper.startPage(pageNo,pageSize);
        Page<CourseInfo> page = courseMapper.findCourseList();
        QueryResult<CourseInfo> queryResult = new QueryResult<>();
        queryResult.setList(page.getResult());
        queryResult.setTotal(page.getTotal());
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,queryResult);
        return queryResponseResult;
    }

    @Transactional
    public ResponseResult addCourseBase(CourseBase courseBase){
        courseBaseRepository.save(courseBase);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    public CourseBase findCourseBaseById(String courseId){
        Optional<CourseBase> optional = courseBaseRepository.findById(courseId);
        if(optional.isPresent()){
            return optional.get();
        }
        return null;
    }

    @Transactional
    public ResponseResult updateCoursebase(String courseId,CourseBase courseBase) {
        CourseBase origin = this.findCourseBaseById(courseId);
        if(origin!=null){
            origin.setName(courseBase.getName());
            origin.setUsers(courseBase.getUsers());
            origin.setGrade(courseBase.getGrade());
            origin.setStudymodel(courseBase.getStudymodel());
            origin.setMt(courseBase.getMt());
            origin.setSt(courseBase.getSt());
            origin.setDescription(courseBase.getDescription());
            courseBaseRepository.save(origin);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    @Transactional
    public ResponseResult addCoursePic(String courseId, String pic) {
        CoursePic coursePic = null;
        Optional<CoursePic> optional = coursePicRepository.findById(courseId);
        if(optional.isPresent()){
            coursePic = optional.get();
        }
        if(coursePic == null){
            coursePic = new CoursePic();
            coursePic.setCourseid(courseId);
        }
        coursePic.setPic(pic);
        coursePicRepository.save(coursePic);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    public CoursePic findCoursePic(String coursdId) {
        Optional<CoursePic> optional = coursePicRepository.findById(coursdId);
        if(optional.isPresent()){
            return optional.get();
        }
        return null;
    }

    @Transactional
    public ResponseResult deleteCoursePic(String courseId) {
        //TODO 删除fastdfs上的图片
        long result = coursePicRepository.deleteByCourseid(courseId);
        if(result > 0){
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    public CourseView getCourseView(String id) {
        CourseView courseView = new CourseView();
        Optional<CourseBase> courseBaseOptional = courseBaseRepository.findById(id);
        if(courseBaseOptional.isPresent()){
            courseView.setCourseBase(courseBaseOptional.get());
        }

        Optional<CoursePic> optionalCoursePic = coursePicRepository.findById(id);
        if(optionalCoursePic.isPresent()){
            courseView.setCoursePic(optionalCoursePic.get());
        }

        Optional<CourseMarket> optionalCourseMarket = courseMarketRepository.findById(id);
        if(optionalCourseMarket.isPresent()){
            courseView.setCourseMarket(optionalCourseMarket.get());
        }

        TeachplanNode teachplanNode = teachplanMapper.selectList(id);
        courseView.setTeachplanNode(teachplanNode);

        return courseView;
    }

    public CoursePublishResult preview(String id) {

        CmsPage cmsPage = buildCmsPage(id);

        CmsPageResult cmsPageResult = cmsPageClient.saveCmsPage(cmsPage);
        if(!cmsPageResult.isSuccess()){
            return new CoursePublishResult(CommonCode.FAIL,null);
        }
        String url = previewUrl + cmsPageResult.getCmsPage().getPageId();
        return new CoursePublishResult(CommonCode.SUCCESS,url);
    }

    @Transactional
    public CoursePublishResult publish(String id) {

        CmsPage cmsPage = buildCmsPage(id);
        CmsPostPageResult cmsPostPageResult = cmsPageClient.postPageQuick(cmsPage);
        if(!cmsPostPageResult.isSuccess()){
            return new CoursePublishResult(CommonCode.FAIL,null);
        }

        //更新课程发布状态为已发布
        CourseBase courseBase = this.updateCourseStatus(id);
        if(courseBase==null){
            return new CoursePublishResult(CommonCode.FAIL,null);
        }

        //保存课程索引信息
        CoursePub coursePub = createCoursePub(id);
        saveCoursePub(id,coursePub);
        //缓存课程信息
        //TODO
        return new CoursePublishResult(CommonCode.SUCCESS,cmsPostPageResult.getPageUrl());
    }

    private CmsPage buildCmsPage(String id){
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId(siteId);
        cmsPage.setDataUrl(dataUrlPre+id);
        cmsPage.setPageName(id+".html");
        cmsPage.setPageAliase(this.findCourseBaseById(id).getName());
        cmsPage.setPagePhysicalPath(pagePhysicalPath);
        cmsPage.setPageWebPath(pageWebPath);
        cmsPage.setTemplateId(templateId);
        return cmsPage;
    }

    private CourseBase updateCourseStatus(String courseId){
        CourseBase courseBase = this.findCourseBaseById(courseId);
        courseBase.setStatus("202002");
        courseBaseRepository.save(courseBase);
        return courseBase;
    }

    private CoursePub createCoursePub(String courseId){
        CoursePub coursePub = new CoursePub();
        //courseBase
        Optional<CourseBase> baseOptional = courseBaseRepository.findById(courseId);
        if(baseOptional.isPresent()){
            CourseBase courseBase = baseOptional.get();
            BeanUtils.copyProperties(courseBase,coursePub);
        }
        //coursePic
        Optional<CoursePic> picOptional = coursePicRepository.findById(courseId);
        if(picOptional.isPresent()){
            CoursePic coursePic = picOptional.get();
            BeanUtils.copyProperties(coursePic,coursePub);
        }
        //coursePic
        Optional<CourseMarket> marketOptional = courseMarketRepository.findById(courseId);
        if(marketOptional.isPresent()){
            CourseMarket courseMarket = marketOptional.get();
            BeanUtils.copyProperties(courseMarket,coursePub);
        }
        //teachplan
        TeachplanNode teachplanNode = teachplanMapper.selectList(courseId);
        String planJson = JSON.toJSONString(teachplanNode);
        coursePub.setTeachplan(planJson);
        return coursePub;
    }

    private CoursePub saveCoursePub(String id,CoursePub coursePub){
        CoursePub coursePubNew = null;
        Optional<CoursePub> coursePubOptional = coursePubRepository.findById(id);
        if(coursePubOptional.isPresent()){
            coursePubNew = coursePubOptional.get();
        }else{
            coursePubNew = new CoursePub();
        }
        BeanUtils.copyProperties(coursePub,coursePubNew);
        Date now = new Date();
        coursePubNew.setTimestamp(now);
        coursePubNew.setId(id);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        coursePubNew.setPubTime(format.format(now));
        coursePubRepository.save(coursePubNew);
        return coursePubNew;

    }
}

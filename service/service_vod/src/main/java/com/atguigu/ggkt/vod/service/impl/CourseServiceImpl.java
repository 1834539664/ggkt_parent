package com.atguigu.ggkt.vod.service.impl;

import com.atguigu.ggkt.model.vod.Course;
import com.atguigu.ggkt.model.vod.CourseDescription;
import com.atguigu.ggkt.model.vod.Subject;
import com.atguigu.ggkt.model.vod.Teacher;
import com.atguigu.ggkt.vo.vod.CourseFormVo;
import com.atguigu.ggkt.vo.vod.CoursePublishVo;
import com.atguigu.ggkt.vo.vod.CourseQueryVo;
import com.atguigu.ggkt.vod.mapper.CourseMapper;
import com.atguigu.ggkt.vod.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-11-25
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private CourseDescriptionService courseDescriptionService;
    @Autowired
    private VideoService videoService;
    @Autowired
    private ChapterService chapterService;

    //点播课程列表
    @Override
    public Map<String, Object> findPageCourse(Page<Course> pageParam, CourseQueryVo courseQueryVo) {
        LambdaQueryWrapper<Course> queryWrapper = new LambdaQueryWrapper<>();
        if (courseQueryVo != null) {
            //获取条件值
            Long subjectId = courseQueryVo.getSubjectId();
            String title = courseQueryVo.getTitle();
            Long teacherId = courseQueryVo.getTeacherId();
            Long subjectParentId = courseQueryVo.getSubjectParentId();
            //插入条件
            queryWrapper.eq(!StringUtils.isEmpty(subjectId), Course::getSubjectId, subjectId);
            queryWrapper.like(!StringUtils.isEmpty(title), Course::getTitle, title);
            queryWrapper.eq(!StringUtils.isEmpty(teacherId), Course::getTeacherId, teacherId);
            queryWrapper.eq(!StringUtils.isEmpty(subjectParentId), Course::getSubjectParentId, subjectParentId);
        }
        //查询
        Page<Course> pages = baseMapper.selectPage(pageParam, queryWrapper);
        long totalCount = pages.getTotal();
        long totalPage = pages.getPages();
        List<Course> list = pages.getRecords();

        //查询数据里面的有几个id : 讲师id 课程分类id(一层 二层)
        //获取这些id获取的名称,进行封装,最终显示
        list.stream().forEach(item -> {
            this.getNameById(item);
        });

        //封装返回值
        HashMap<String, Object> map = new HashMap<>();
        map.put("totalCount", totalCount);
        map.put("totalPage", totalPage);
        map.put("records", list);
        return map;
    }

    @Override
    public Course getNameById(Course course) {
        //根据讲师的id获取讲师的名称
        Teacher teacher = teacherService.getById(course.getTeacherId());
        if (teacher != null) {
            String name = teacher.getName();
            course.getParam().put("teacherName", name);
        }
        //根据课程分类的id获取课程分类名称
        Subject subjectOne = subjectService.getById(course.getSubjectParentId());
        if (subjectOne != null) {
            course.getParam().put("subjectParentTitle", subjectOne.getTitle());
        }

        Subject subjectTwo = subjectService.getById(course.getSubjectId());
        if (subjectOne != null) {
            course.getParam().put("subjectTitle", subjectOne.getTitle());
        }
        return course;
    }

    @Override
    public Long saveCourseInfo(CourseFormVo courseFormVo) {
        //添加课程基本信息,course表
        Course course = new Course();
        BeanUtils.copyProperties(courseFormVo, course);
        baseMapper.insert(course);

        //添加课程的描述信息,course_description表
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setDescription(courseFormVo.getDescription());
        //设置课程id
        courseDescription.setId(course.getId());
        courseDescriptionService.save(courseDescription);
        return course.getId();
    }

    //根据id查询课程信息
    @Override
    public CourseFormVo getCourseInfoById(Long id) {
        //课程的基本信息
        Course course = baseMapper.selectById(id);
        if (course == null) {
            return null;
        }
        //课程的描述信息
        CourseDescription courseDescription = courseDescriptionService.getById(id);
        CourseFormVo courseFormVo = new CourseFormVo();
        BeanUtils.copyProperties(course, courseFormVo);
        if (courseDescription!=null){
            courseFormVo.setDescription(courseDescription.getDescription());
        }
        return courseFormVo;
    }

    @Override
    public void updateCourseById(CourseFormVo courseFormVo) {
        //修改课程基本信息
        Course course = new Course();
        BeanUtils.copyProperties(courseFormVo,course);
        baseMapper.updateById(course);
        //修改课程描述信息
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setDescription(courseFormVo.getDescription());
        courseDescription.setId(course.getId());
        courseDescriptionService.updateById(courseDescription);
    }

    @Override
    public CoursePublishVo getCoursePublishVo(Long id) {

        return baseMapper.selectCoursePublishVoById(id);
    }

    //课程最终发布
    @Override
    public void publishCourse(Long id) {
        Course course = baseMapper.selectById(id);
        course.setStatus(1);//课程已经发布
        course.setPublishTime(new Date());
        baseMapper.updateById(course);
    }

    //根据课程id删除课程
    @Override
    public void removeCourseById(Long id) {
        //根据课程id删除小节
        videoService.removeVideoByCourseId(id);
        //根据课程id删除章节
        chapterService.removeChapterByCourseId(id);
        //根据课程id删除描述
        courseDescriptionService.removeById(id);
        //根据课程id删除课程
        baseMapper.deleteById(id);
    }

}
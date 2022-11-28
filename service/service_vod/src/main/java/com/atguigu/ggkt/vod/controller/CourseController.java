package com.atguigu.ggkt.vod.controller;


import com.atguigu.ggkt.model.vod.Course;
import com.atguigu.ggkt.result.Result;
import com.atguigu.ggkt.vo.vod.CourseFormVo;
import com.atguigu.ggkt.vo.vod.CoursePublishVo;
import com.atguigu.ggkt.vo.vod.CourseQueryVo;
import com.atguigu.ggkt.vod.service.CourseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-11-25
 */
@RestController
@RequestMapping("/admin/vod/course")
@Api(tags = "课程接口")
//@CrossOrigin
public class CourseController {
    @Autowired
    private CourseService courseService;

    //删除课程
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable("id")Long id){
        courseService.removeCourseById(id);
        return Result.ok(null);
    }

    //根据课程id查询发布课程信息
    @ApiOperation("根据课程id查询发布课程信息")
    @GetMapping("/getCoursePublishVo/{id}")
    public Result getCoursePublishVo(@PathVariable("id") Long id) {
        CoursePublishVo coursePublishVo = courseService.getCoursePublishVo(id);
        return Result.ok(coursePublishVo);
    }
    //课程的最终发布
    @ApiOperation("课程的最终发布")
    @PutMapping("/publishCourse/{id}")
    public Result publishCourse(@PathVariable("id")Long id){
        courseService.publishCourse(id);
        return Result.ok(null);
    }

    //根据id获取课程信息
    @GetMapping("/get/{id}")
    public Result get(@PathVariable("id") Long id) {
        CourseFormVo courseFormVo = courseService.getCourseInfoById(id);
        return Result.ok(courseFormVo);
    }

    @PostMapping("/update")
    public Result update(@RequestBody CourseFormVo courseFormVo) {
        courseService.updateCourseById(courseFormVo);
        //课程id
        return Result.ok(courseFormVo.getId());
    }

    //添加课程基本信息
    @ApiOperation("添加课程基本信息")
    @PostMapping("/save")
    public Result save(@RequestBody CourseFormVo courseFormVo) {
        Long courseId = courseService.saveCourseInfo(courseFormVo);
        return Result.ok(courseId);
    }

    //点播课程列表
    @ApiOperation("点播课程列表")
    @GetMapping("/{page}/{limit}")
    public Result courseList(@PathVariable("page") Long page,
                             @PathVariable("limit") Long limit,
                             CourseQueryVo courseQueryVo) {
        Page<Course> pageParam = new Page<>(page, limit);
        Map<String, Object> map = courseService.findPageCourse(pageParam, courseQueryVo);
        return Result.ok(map);
    }

}


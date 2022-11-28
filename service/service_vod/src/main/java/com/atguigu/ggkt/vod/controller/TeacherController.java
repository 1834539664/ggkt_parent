package com.atguigu.ggkt.vod.controller;


import com.atguigu.ggkt.exception.GgktException;
import com.atguigu.ggkt.model.vod.Teacher;
import com.atguigu.ggkt.result.Result;
import com.atguigu.ggkt.vo.vod.TeacherQueryVo;
import com.atguigu.ggkt.vod.service.TeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-11-22
 */
@Api(tags = "讲师管理接口")
@RestController //@Controller and @ResponseBody(将返回的对象转换为Json格式)
@RequestMapping("/admin/vod/teacher")
//@CrossOrigin //解决跨域问题
public class TeacherController {

    @Autowired
    private TeacherService teacherService;


    //1.查询所有的讲师 http://localhost:8301//admin/vod/teacher/list
    //@ApiOperation( "查询所有讲师")
    //@GetMapping("/findAllTeacher")
    //public List<Teacher> findAllTeacher() {
    //    return teacherService.list();
    //}

    //1.查询所有的讲师 http://localhost:8301//admin/vod/teacher/list
    @ApiOperation("查询所有讲师")
    @GetMapping("/findAllTeacher")
    public Result findAllTeacher() {
        //try {
        //    int i = 1/0;
        //}catch (Exception e){
        //    throw new GgktException(599,"自定义异常");
        //}

        List<Teacher> list = teacherService.list();
        return Result.ok(list).message("查询数据成功");
    }


    //2.逻辑删除讲师
    @ApiOperation("逻辑删除讲师")
    @DeleteMapping("/remove/{id}")
    public Result removeTeacher(@ApiParam(name = "id", value = "ID", required = true)
                                @PathVariable("id") Long id) {
        boolean isSuccess = teacherService.removeById(id);
        return Result.judge(isSuccess, null);
    }

    //3.分页条件查询讲师
    @ApiOperation("分页条件查询讲师")
    @PostMapping("/findQueryPage/{current}/{limit}") //注:@RequestBody要与post一起使用
    public Result findQueryPage(@PathVariable Long current,
                                @PathVariable Long limit,
                                @RequestBody(required = false) TeacherQueryVo teacherQueryVo) {
        //创建分页对象
        Page<Teacher> page = new Page<>(current, limit);
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        //判断teacherQueryVo对象是否为空
        queryWrapper.orderByAsc("id");//排序
        if (teacherQueryVo != null) {
            //进行非空判断,条件封装
            queryWrapper.like(teacherQueryVo.getName() != null, "name", teacherQueryVo.getName());
            queryWrapper.eq(teacherQueryVo.getLevel() != null, "level", teacherQueryVo.getLevel());
            queryWrapper.ge(teacherQueryVo.getJoinDateBegin() != null, "join_date", teacherQueryVo.getJoinDateBegin());
            queryWrapper.le(teacherQueryVo.getJoinDateEnd() != null, "join_date", teacherQueryVo.getJoinDateEnd());
        }
        //添加分页条件,查询数据库
        teacherService.page(page, queryWrapper);
        return Result.ok(page);
    }

    //4.添加讲师
    @ApiOperation("添加讲师")
    @PostMapping("/saveTeacher")
    public Result saveTeacher(@RequestBody Teacher teacher) {
        boolean isSuccess = teacherService.save(teacher);
        return Result.judge(isSuccess, null);
    }

    //5.根据id查询
    @ApiOperation("根据id查询")
    @GetMapping("/getTeacher/{id}")
    public Result getTeacher(@PathVariable("id") Long id) {
        Teacher teacher = teacherService.getById(id);
        return Result.ok(teacher);
    }

    //6.修改老师
    @ApiOperation("修改老师")
    @PostMapping("/updateTeacher")
    public Result updateTeacher(@RequestBody Teacher teacher) {
        boolean isSuccess = teacherService.updateById(teacher);
        return Result.judge(isSuccess, null);
    }

    //7.批量删除老师
    //以json数组格式传递参数
    @ApiOperation("批量删除老师")
    @DeleteMapping("/removeBatch")
    public Result removeBatch(@RequestBody List<Long> idList) {
        boolean isSuccess = teacherService.removeByIds(idList);
        return Result.judge(isSuccess, null);
    }
}


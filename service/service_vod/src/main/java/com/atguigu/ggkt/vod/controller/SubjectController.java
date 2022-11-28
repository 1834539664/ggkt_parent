package com.atguigu.ggkt.vod.controller;


import com.atguigu.ggkt.model.vod.Subject;
import com.atguigu.ggkt.result.Result;
import com.atguigu.ggkt.vod.service.SubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-11-25
 */
@Api(tags = "分类接口")
@RestController
@RequestMapping("/admin/vod/subject")
//@CrossOrigin
public class SubjectController {

    @Autowired
    SubjectService subjectService;

    //课程分类的列表接口
    //懒加载,每次只查询一层数据
    @ApiOperation("课程分类的列表")
    @GetMapping("/getChildSubject/{id}")
    public Result getChildSubject(@PathVariable("id")Long id){
        List<Subject> list= subjectService.selectSubjectList(id);
        return Result.ok(list);
    }

    //课程分类导出功能
    @ApiOperation("课程分类的导出")
    @GetMapping("/exportData")
    public void exportData(HttpServletResponse response){
        subjectService.exportData(response);
    }

    //课程分类的导入功能
    @ApiOperation("课程分类的导入功能")
    @PostMapping("/importData")
    public Result importData(MultipartFile file){
        subjectService.importData(file);
        return Result.ok(null);
    }
}


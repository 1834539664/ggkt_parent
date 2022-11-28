package com.atguigu.ggkt.vod.controller;


import com.atguigu.ggkt.model.vod.Chapter;
import com.atguigu.ggkt.result.Result;
import com.atguigu.ggkt.vo.vod.ChapterVo;
import com.atguigu.ggkt.vod.service.ChapterService;
import com.sun.org.apache.regexp.internal.RE;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-11-25
 */
@RestController
@RequestMapping("/admin/vod/chapter")
//@CrossOrigin
@Api(tags = "章节接口")
public class ChapterController {
    @Autowired
    private ChapterService chapterService;

    //大纲列表
    @ApiOperation("大纲列表")
    @GetMapping("getNestedTreeList/{courseId}")
    public Result getTreeList(@PathVariable("courseId")Long courseId){
        List<ChapterVo> list = chapterService.getTreeList(courseId);
        return Result.ok(list);
    }
    //添加章节
    @PostMapping("/save")
    public Result save(@RequestBody Chapter chapter){
        chapterService.save(chapter);
        return Result.ok(null);
    }
    //根据id查询
    @GetMapping("/get/{id}")
    public Result get(@PathVariable("id") Long id){
        Chapter chapter = chapterService.getById(id);
        return Result.ok(chapter);
    }
    //修改
    @PostMapping("update")
    public Result update(@RequestBody Chapter chapter){
        chapterService.updateById(chapter);
        return Result.ok(null);
    }
    //删除章节
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable("id")Long id){
        chapterService.removeById(id);
        return Result.ok(null);
    }
}


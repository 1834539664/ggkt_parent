package com.atguigu.ggkt.vod.service.impl;


import com.alibaba.excel.EasyExcel;
import com.atguigu.ggkt.exception.GgktException;
import com.atguigu.ggkt.model.vod.Subject;
import com.atguigu.ggkt.vo.vod.SubjectEeVo;
import com.atguigu.ggkt.vo.vod.SubjectVo;
import com.atguigu.ggkt.vod.listener.SubjectListener;
import com.atguigu.ggkt.vod.mapper.SubjectMapper;
import com.atguigu.ggkt.vod.service.SubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-11-25
 */
@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {
    @Autowired
    private SubjectListener subjectListener;
    /**
     * 课程分类的列表接口
     * 懒加载,每次只查询一层数据
     * @param id parentId
     * @return
     */
    @Override
    public List<Subject> selectSubjectList(Long id) {
        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id",id);
        List<Subject> list = baseMapper.selectList(queryWrapper);
        //TODO
        for (Subject subject : list) {
            Long subjectId = subject.getId();
            //查询
            boolean isChild = this.isChildren(subjectId);
            subject.setHasChildren(isChild);
        }
        return list;
    }

    @Override
    public boolean isChildren(Long subjectId) {
        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id",subjectId);
        Integer count = baseMapper.selectCount(queryWrapper);
        return count > 0;
    }

    //课程分类的导出
    @Override
    public void exportData(HttpServletResponse response) {
        //设置下载信息
        try{
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("课程分类", "UTF-8");
            response.setHeader("Content-disposition",
                    "attachment;filename="+ fileName + ".xlsx");

            //查询课程分类表中的所有数据
            List<Subject> subjectList = baseMapper.selectList(null);
            //转换Subject -->SubjectEeVo
            ArrayList<SubjectEeVo> list = new ArrayList<>();
            for (Subject subject : subjectList) {
                SubjectEeVo subjectEeVo = new SubjectEeVo();
                //把一个对象中的值复制到另一个对象,注意这个方法时浅拷贝,
                // 如果是引用对象,要注意一点
                BeanUtils.copyProperties(subject,subjectEeVo);
                list.add(subjectEeVo);
            }
            //用EasyExcel进行写操作
            EasyExcel.write(response.getOutputStream(), SubjectEeVo.class)
                    .sheet("课程分类").doWrite(list);

        }catch(Exception e){
            throw new GgktException(20001,"导出失败");
        }

    }

    @Override
    public void importData(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(),SubjectEeVo.class,subjectListener)
                    .sheet().doRead();
        } catch (IOException e) {
            throw new GgktException(20001,"导入失败");
        }
    }
}

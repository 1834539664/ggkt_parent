package com.atguigu.ggkt.vod.service;

import com.atguigu.ggkt.model.vod.Subject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author atguigu
 * @since 2022-11-25
 */
public interface SubjectService extends IService<Subject> {

    List<Subject> selectSubjectList(Long id);

    /**
     * 根据id查询某一个表单项是否有子项
     * @param subjectId
     * @return
     */
    boolean isChildren(Long subjectId);

    void exportData(HttpServletResponse response);

    /**
     * 课程分类的导入
     * @param file
     */
    void importData(MultipartFile file);

}

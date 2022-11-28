package com.atguigu.ggkt.vod.service.impl;

import com.atguigu.ggkt.model.vod.Video;
import com.atguigu.ggkt.vod.mapper.VideoMapper;
import com.atguigu.ggkt.vod.service.VideoService;
import com.atguigu.ggkt.vod.service.VodService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-11-25
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {
    @Autowired
    private VodService vodService;
    @Override
    public void removeVideoByCourseId(Long id) {
        //根据课程id查询课程所有小节
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id",id);
        List<Video> videoList = baseMapper.selectList(queryWrapper);
        //遍历所有小节的集合得到每个小节,获取每个小节的视频id
        for (Video video : videoList) {
            //获取每个小节视频id
            String videoSourceId = video.getVideoSourceId();
            //判断视频id是否为空,不为空,删视频
            if (!StringUtils.isEmpty(videoSourceId)){
                vodService.removeVideo(videoSourceId);
            }
        }
        //根据课程id书喊出课程所有小节
        baseMapper.delete(queryWrapper);
    }
    //删除小节 同时删除小节里面的视频
    @Override
    public void removeVideoById(Long id) {
        //id查询
        Video video = baseMapper.selectById(id);
        String videoSourceId = video.getVideoSourceId();
        if (!StringUtils.isEmpty(videoSourceId)){
            vodService.removeVideo(videoSourceId);
        }
        //根据id删除小节
        baseMapper.deleteById(id);
    }
}

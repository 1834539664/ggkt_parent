package com.atguigu.ggkt.vod.service;

/**
 * @author WH
 * @version 1.0
 * @date 2022/11/27 15:18
 */
public interface VodService {
    String uploadVideo();

    void removeVideo(String fileId);
}

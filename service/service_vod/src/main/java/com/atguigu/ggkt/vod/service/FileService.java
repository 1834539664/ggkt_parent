package com.atguigu.ggkt.vod.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author WH
 * @version 1.0
 * @date 2022/11/24 22:18
 */
public interface FileService {
    //文件上传
    String upload(MultipartFile file);
}

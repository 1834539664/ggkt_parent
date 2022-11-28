package com.atguigu.ggkt.vod.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author WH
 * @version 1.0
 * @date 2022/11/22 19:43
 */
@Configuration
@MapperScan("com.atguigu.ggkt.vod.mapper") //扫描mapper包所在的地方
public class VodConfig {
    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}

package com.atguigu.ggkt.vod;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author WH
 * @version 1.0
 * @date 2022/11/22 19:43
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = "com.atguigu")//包扫描规则,如果不配置,不是本工程的包将扫描不到,无法加载,swagger2等将无法使用
public class ServiceVodApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceVodApplication.class, args);
    }
}

package com.qqspeed;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * 项目启动类
 */
@SpringBootApplication
@MapperScan("com.qqspeed.mapper")  // 扫描Mapper接口
@EnableCaching  // 开启缓存（Redis）
public class QqspeedSourceApplication {
    public static void main(String[] args) {
        SpringApplication.run(QqspeedSourceApplication.class, args);
        System.out.println("===== QQ飞车手游资料站后端启动成功 =====");
    }
}
package com.atguigu.eduservice.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * TODO
 *
 * @ClassName: EduConfig
 * @author: yan
 * @since: 2021/3/30 15:00
 */
@Configuration
@MapperScan("com.atguigu.eduservice.mapper")
public class EduConfig {
}

package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.vo.TeacherQuery;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author yp
 * @since 2021-03-29
 */
public interface EduTeacherService extends IService<EduTeacher> {


    Map<String, Object> getTeacherList(long current, long limit, TeacherQuery teacherQuery);
}

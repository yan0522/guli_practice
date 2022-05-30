package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.entity.vo.CourseQuery;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author yp
 * @since 2021-06-21
 */
public interface EduCourseService extends IService<EduCourse> {

    //添加课程基本信息
    String saveCourseInfo(CourseInfoVo courseInfoVo);

    //根据课程id查询课程基本信息
    CourseInfoVo getCourseInfo(String courseId);

    //根据课程id修改课程基本信息
    void updateCourseInfo(CourseInfoVo courseInfoVo);

    //根据课程id查询课程预发布确认信息
    CoursePublishVo publishCourseInfo(String id);

    //课程删除
    void removeCourse(String courseId);

    //多条件组合查询课程分页
    Map<String, Object> getCourseList(long current, long limit, CourseQuery courseQuery);
}

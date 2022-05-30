package com.atguigu.eduservice.mapper;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author yp
 * @since 2021-06-21
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {

    //获取预发布页面显示信息
    public CoursePublishVo getCoursePublishVo(String courseId);
}

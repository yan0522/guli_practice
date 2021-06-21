package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.service.EduCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author yp
 * @since 2021-06-21
 */
@RestController
@RequestMapping("/eduservice/course")
public class EduCourseController {

    @Autowired
    private EduCourseService courseService;

    //添加课程基本信息
    @PostMapping("addCourseInfo")
    public R addCourseInfo(@RequestBody CourseInfoVo courseInfoVo) {
        //返回添加之后课程id，为了方便后面课程大纲使用
        String id = courseService.saveCourseInfo(courseInfoVo);
        return R.ok().data("courseId",id);

    }
}


package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduCourseDescription;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.entity.vo.CourseQuery;
import com.atguigu.eduservice.mapper.EduCourseMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCourseDescriptionService;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.exception.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author yp
 * @since 2021-06-21
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    @Autowired
    private EduCourseDescriptionService eduCourseDescriptionService;

    @Autowired
    private EduChapterService eduChapterService;

    @Autowired
    private EduVideoService eduVideoService;

    //添加课程基本信息
    @Override
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {
        //1向课程表添加课程基本信息
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        int insert = baseMapper.insert(eduCourse);
        if (insert == 0) {
            throw new GuliException(20000,"添加课程信息失败");
        }
        //获取添加课程id
        String cid = eduCourse.getId();

        //2向简介表添加课程简介信息
        EduCourseDescription courseDescription = new EduCourseDescription();
        courseDescription.setDescription(courseInfoVo.getDescription());
        courseDescription.setId(cid);
        eduCourseDescriptionService.save(courseDescription);

        return cid;
    }

    //根据课程id查询课程基本信息
    @Override
    public CourseInfoVo getCourseInfo(String courseId) {
        EduCourse eduCourse = baseMapper.selectById(courseId);
        CourseInfoVo courseInfoVo = new CourseInfoVo();
        BeanUtils.copyProperties(eduCourse,courseInfoVo);

        EduCourseDescription courseDescription = eduCourseDescriptionService.getById(courseId);
        courseInfoVo.setDescription(courseDescription.getDescription());
        return courseInfoVo;
    }

    //根据课程id修改课程基本信息
    @Override
    public void updateCourseInfo(CourseInfoVo courseInfoVo) {
        //修改课程表里的信息
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        int update = baseMapper.updateById(eduCourse);
        if (update == 0) {
            throw new GuliException(20001,"修改课程基本信息失败");
        }
        //修改描述表里的信息
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        eduCourseDescription.setId(courseInfoVo.getId());
        eduCourseDescription.setDescription(courseInfoVo.getDescription());
        eduCourseDescriptionService.updateById(eduCourseDescription);
    }

    //根据课程id查询课程预发布确认信息
    @Override
    public CoursePublishVo publishCourseInfo(String id) {
        CoursePublishVo publishCourseInfo = baseMapper.getCoursePublishVo(id);
        return publishCourseInfo;
    }

    //课程删除
    @Override
    public void removeCourse(String courseId) {
        //1根据课程ID删除小节
        eduVideoService.removeVideoByCourseId(courseId);

        //2根据课程ID删除章节
        eduChapterService.removeChapterByCourseId(courseId);

        //3根据课程ID删除描述
        eduCourseDescriptionService.removeById(courseId);

        //4删除课程本身
        int delete = baseMapper.deleteById(courseId);
        if (delete == 0) {
            throw new GuliException(20001,"删除课程失败");
        }
    }

    //多条件组合查询课程分页
    @Override
    public Map<String, Object> getCourseList(long current, long limit, CourseQuery courseQuery) {
        Page<EduCourse> pageCourse = new Page<>(current,limit);
        QueryWrapper<EduCourse> courseQueryWrapper = new QueryWrapper<>();
        //构建多条件组合查询
        String title = courseQuery.getTitle();
        String teacherId = courseQuery.getTeacherId();
        String subjectParentId = courseQuery.getSubjectParentId();
        String subjectId = courseQuery.getSubjectId();
        String status = courseQuery.getStatus();
        if (!StringUtils.isEmpty(title)) {
            courseQueryWrapper.like("title",title);
        }
        if (!StringUtils.isEmpty(teacherId)) {
            courseQueryWrapper.eq("teacher_id",teacherId);
        }
        if (!StringUtils.isEmpty(status)) {
            courseQueryWrapper.eq("status",status);
        }
        if (!StringUtils.isEmpty(subjectParentId)) {
            courseQueryWrapper.eq("subject_Parent_id",subjectParentId);
        }
        if (!StringUtils.isEmpty(subjectId)) {
            courseQueryWrapper.eq("subject_id",subjectId);
        }
        baseMapper.selectPage(pageCourse,courseQueryWrapper);
        long total = pageCourse.getTotal();
        List<EduCourse> records = pageCourse.getRecords();

        Map<String,Object> map = new HashMap<>();
        map.put("total",total);
        map.put("rows",records);
        return map;
    }
}

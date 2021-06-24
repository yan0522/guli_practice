package com.atguigu.eduservice.entity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * TODO
 *
 * @ClassName: CoursePublishVo
 * @author: yan
 * @since: 2021/6/24 13:40
 */
@Data
public class CoursePublishVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String title;
    private String cover;
    private Integer lessonNum;
    private String subjectLevelOne;
    private String subjectLevelTwo;
    private String teacherName;
    private String price;//只用于显示
}

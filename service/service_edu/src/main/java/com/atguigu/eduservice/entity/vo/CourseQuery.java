package com.atguigu.eduservice.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * TODO
 *
 * @ClassName: CourseQuery
 * @author: yan
 * @since: 2021/6/24 23:23
 */
@ApiModel(value = "Course查询对象", description = "课程查询对象封装")
@Data
public class CourseQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "课程名称")
    private String title;

    @ApiModelProperty(value = "讲师id")
    private String teacherId;

    @ApiModelProperty(value = "课程状态 Draft未发布 Normal已发布")
    private String status;

    @ApiModelProperty(value = "一级类别id")
    private String subjectParentId;

    @ApiModelProperty(value = "二级类别id")
    private String subjectId;
}

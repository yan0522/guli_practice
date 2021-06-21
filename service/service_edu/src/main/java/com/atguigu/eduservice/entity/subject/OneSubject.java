package com.atguigu.eduservice.entity.subject;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @ClassName: OneSubject
 * @author: yan
 * @since: 2021/6/21 14:47
 */
//一级分类
@Data
public class OneSubject {
    private String id;
    private String title;
    //一个一级分类有多个二级分类
    private List<TwoSubject> children = new ArrayList<>();
}

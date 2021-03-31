package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.service.EduTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author yp
 * @since 2021-03-29
 */
@RestController
@RequestMapping("/eduservice/teacher")
@Api(tags = "讲师管理")
public class EduTeacherController {

    @Autowired
    private EduTeacherService eduTeacherService;
    //查询所有讲师
    @ApiOperation("查询讲师列表")
    @GetMapping("findAll")
    public R findAllTeacher() {
        List<EduTeacher> list = eduTeacherService.list(null);
        return R.ok().data("items",list);
    }

    //逻辑删除讲师
    @ApiOperation("逻辑删除讲师")
    @DeleteMapping("{id}")
    public R removeTeacher(
            @ApiParam(name = "id",value = "讲师ID",required = true)
            @PathVariable String id) {
        boolean flag = eduTeacherService.removeById(id);
        return flag?R.ok():R.error();
    }
}


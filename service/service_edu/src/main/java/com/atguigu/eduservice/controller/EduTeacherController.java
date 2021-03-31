package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.vo.TeacherQuery;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @ApiOperation("分页查询讲师列表")
    @GetMapping("pageTeacher/{current}/{limit}")
    public R pageTeacher(
            @ApiParam("当前页")
            @PathVariable long current,
            @ApiParam("每页显示数量")
            @PathVariable long limit
    ) {
        Page<EduTeacher> pageTeacher = new Page<>(current,limit);
        eduTeacherService.page(pageTeacher,null);
        long total = pageTeacher.getTotal();
        List<EduTeacher> records = pageTeacher.getRecords();
        return R.ok().data("total",total).data("rows",records);
    }

    @ApiOperation("多条件组合查询讲师分页")
    @PostMapping("getTeacherPageList/{current}/{limit}")
    public R getTeacherPageList(@ApiParam("当前页")
                                @PathVariable long current,
                                @ApiParam("每页显示数量")
                                @PathVariable long limit,
                                @ApiParam("查询条件封装的对象")
                                @RequestBody(required = false) TeacherQuery teacherQuery) {

        Map<String,Object> map = eduTeacherService.getTeacherList(current,limit,teacherQuery);
        return R.ok().data(map);
    }

    /*@ApiOperation("多条件组合查询讲师分页")
    @PostMapping("pageTeacherCondition/{current}/{limit}")
    public R pageTeacherCondition(
            @ApiParam("当前页")
            @PathVariable long current,
            @ApiParam("每页显示数量")
            @PathVariable long limit,
            @ApiParam("查询条件封装的对象")
            @RequestBody(required = false)
            TeacherQuery teacherQuery
            ) {
        Page<EduTeacher> pageTeacher = new Page<>(current,limit);
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        //构建多条件组合查询
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        if (!StringUtils.isEmpty(name)) {
            wrapper.like("name",name);
        }
        if (!StringUtils.isEmpty(level)) {
            wrapper.eq("level",level);
        }
        if (!StringUtils.isEmpty(begin)) {
            wrapper.ge("gmt_create",begin);
        }
        if (!StringUtils.isEmpty(end)) {
            wrapper.le("gmt_create",end);
        }
        eduTeacherService.page(pageTeacher,wrapper);
        long total = pageTeacher.getTotal();
        List<EduTeacher> records = pageTeacher.getRecords();
        return R.ok().data("total",total).data("rows",records);
    }*/

    //逻辑删除讲师
    @ApiOperation("逻辑删除讲师")
    @DeleteMapping("{id}")
    public R removeTeacher(
            @ApiParam(name = "id",value = "讲师ID",required = true)
            @PathVariable String id) {
        boolean flag = eduTeacherService.removeById(id);
        return flag?R.ok():R.error();
    }

    @ApiOperation("添加讲师")
    @PostMapping("addTeacher")
    public R addTeacher(@ApiParam("讲师信息") @RequestBody EduTeacher eduTeacher) {
        boolean save = eduTeacherService.save(eduTeacher);
        return save?R.ok():R.error();
    }
}


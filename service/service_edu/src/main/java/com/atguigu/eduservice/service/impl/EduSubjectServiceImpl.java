package com.atguigu.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.excel.SubjectData;
import com.atguigu.eduservice.entity.subject.OneSubject;
import com.atguigu.eduservice.entity.subject.TwoSubject;
import com.atguigu.eduservice.listener.SubjectExcelListener;
import com.atguigu.eduservice.mapper.EduSubjectMapper;
import com.atguigu.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author yp
 * @since 2021-06-17
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    @Override
    public void saveSubject(MultipartFile file,EduSubjectService subjectService) {

        try {
            //文件输入流
            InputStream in = file.getInputStream();
            //调用方法进行读取
            EasyExcel.read(in, SubjectData.class,new SubjectExcelListener(subjectService)).sheet().doRead();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    //课程分类列表树形显示
    @Override
    public List<OneSubject> getAllOneTwoSubject() {
        //查询所有一级分类,pid =0
        QueryWrapper<EduSubject> queryWrapperOne = new QueryWrapper<>();
        queryWrapperOne.eq("parent_id","0");
        List<EduSubject> oneSubjectList = baseMapper.selectList(queryWrapperOne);


        //查询所有二级分类pid !=0
        QueryWrapper<EduSubject> queryWrapperTwo = new QueryWrapper<>();
        queryWrapperTwo.ne("parent_id","0");
        List<EduSubject> twoSubjectList = baseMapper.selectList(queryWrapperTwo);

        //创建一个集合用于封装最终数据
        List<OneSubject> finalSubjectList = new ArrayList<>();
        //封装一级分类
        for (int i = 0; i < oneSubjectList.size(); i++) {
            EduSubject eduSubject = oneSubjectList.get(i);
            OneSubject oneSubject = new OneSubject();
            //属性多的时候一个一个set太繁琐
            //oneSubject.setId(eduSubject.getId());
            //oneSubject.setTitle(eduSubject.getTitle());
            //工具类替换上面
            BeanUtils.copyProperties(eduSubject,oneSubject);

            finalSubjectList.add(oneSubject);

            List<TwoSubject> twoFinalSubjectList = new ArrayList<>();
            for (int j = 0; j < twoSubjectList.size(); j++) {
                EduSubject tSubject = twoSubjectList.get(j);

                if (tSubject.getParentId().equals(eduSubject.getId())) {
                    TwoSubject twoSubject = new TwoSubject();
                    BeanUtils.copyProperties(tSubject,twoSubject);
                    twoFinalSubjectList.add(twoSubject);
                }
            }
            oneSubject.setChildren(twoFinalSubjectList);
        }
        //封装二级分类
        return finalSubjectList;
    }
}

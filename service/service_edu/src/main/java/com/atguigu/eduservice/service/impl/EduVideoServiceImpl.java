package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.client.VodClient;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.mapper.EduVideoMapper;
import com.atguigu.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author yp
 * @since 2021-06-21
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {

    //注入远程调用接口
    @Autowired
    private VodClient vodClient;

    @Override
    public void removeVideoById(String id) {
        //先删除阿里云端存储的视频
        //根据课程ID查询小节
        EduVideo eduVideo = baseMapper.selectById(id);
        //得到视频ID
        String videoSourceId = eduVideo.getVideoSourceId();
        //判断视频ID，若不为空，执行删除操作
        if (!StringUtils.isEmpty(videoSourceId)) {
            vodClient.removeAlyVideo(videoSourceId);
        }
        //再删除小节
        baseMapper.deleteById(id);
    }

    //根据课程ID先删除小节下的全部视频，再删除小节
    @Override
    public void removeVideoByCourseId(String courseId) {

        //根据课程id查询课程所有的视频id
        QueryWrapper<EduVideo> wrapperVideo= new QueryWrapper<>();
        wrapperVideo.eq("course_id",courseId);
        wrapperVideo.select("video_source_id");
        List<EduVideo> eduVideoList = baseMapper.selectList(wrapperVideo);

        //将查询到的video_source_id放到List<String>中
        List<String> videoIds = new ArrayList<>();
        for (int i = 0; i < eduVideoList.size(); i++) {
            EduVideo eduVideo = eduVideoList.get(i);
            String videoSourceId = eduVideo.getVideoSourceId();
            if(!StringUtils.isEmpty(videoSourceId)){
                //放到videoIds集合
                videoIds.add(videoSourceId);
            }
        }
        //根据多个视频id删除多个视频
        if(videoIds.size()>0) {
            vodClient.deleteBatch(videoIds);
        }

        QueryWrapper<EduVideo> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("course_id",courseId);
        baseMapper.delete(videoQueryWrapper);
    }
}

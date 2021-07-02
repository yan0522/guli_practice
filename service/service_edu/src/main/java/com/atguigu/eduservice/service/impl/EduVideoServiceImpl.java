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

    //根据课程ID删除小节
    @Override
    public void removeVideoByCourseId(String CourseId) {

        QueryWrapper<EduVideo> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("course_id",CourseId);
        baseMapper.delete(videoQueryWrapper);
    }
}

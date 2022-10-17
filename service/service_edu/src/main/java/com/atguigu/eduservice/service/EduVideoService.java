package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduVideo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author yp
 * @since 2021-06-21
 */
public interface EduVideoService extends IService<EduVideo> {

    //根据小节ID，先删阿里云端视频，再删除小节
    void removeVideoById(String id);

    //根据课程id删除小节
    void removeVideoByCourseId(String courseId);

}

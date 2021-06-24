package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.atguigu.eduservice.entity.chapter.VideoVo;
import com.atguigu.eduservice.mapper.EduChapterMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.exception.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author yp
 * @since 2021-06-21
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    EduVideoService videoService;

    //课程大纲列表，根据课程id查询
    @Override
    public List<ChapterVo> getChapterVideoByCourseId(String courseId) {

        //根据课程id查询课程里所有章节
        QueryWrapper<EduChapter> wrapperChapter = new QueryWrapper<>();
        wrapperChapter.eq("course_id",courseId);
        List<EduChapter> chapterList = baseMapper.selectList(wrapperChapter);

        //根据课程id查询课程里所有小节
        QueryWrapper<EduVideo> wrapperVideo = new QueryWrapper<>();
        wrapperVideo.eq("course_id",courseId);
        List<EduVideo> VideoList = videoService.list(wrapperVideo);

        List<ChapterVo> finalChapterVideoList = new ArrayList<>();
        //遍历查询章节list集合进行封装
        for (int i = 0; i < chapterList.size(); i++) {
            EduChapter eduChapter = chapterList.get(i);
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(eduChapter,chapterVo);
            finalChapterVideoList.add(chapterVo);

            //创建用于封装小节的list集合
            List<VideoVo> videoVoList = new ArrayList<>();
            //遍历查询小节list集合进行封装
            for (int j = 0; j < VideoList.size(); j++) {
                EduVideo eduVideo = VideoList.get(j);

                if (eduVideo.getChapterId().equals(eduChapter.getId())) {
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(eduVideo,videoVo);
                    videoVoList.add(videoVo);
                }
            }
            chapterVo.setChildren(videoVoList);
        }
        return finalChapterVideoList;
    }

    //删除章节
    @Override
    public boolean deleteChapter(String chapterId) {
        QueryWrapper<EduVideo> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("chapter_id",chapterId);
        int count = videoService.count(videoQueryWrapper);
        if (count > 0) {
            throw new GuliException(20001,"章节下仍有小节,无法直接删除");
        }else {
            int result = baseMapper.deleteById(chapterId);
            return result > 0;
        }
    }

    //根据课程ID删除章节
    @Override
    public void removeChapterByCourseId(String courseId) {
        QueryWrapper<EduChapter> chapterQueryWrapper = new QueryWrapper<>();
        chapterQueryWrapper.eq("course_id",courseId);
        baseMapper.delete(chapterQueryWrapper);
    }
}

package com.atguigu.eduservice.entity.chapter;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @ClassName: ChapterVo
 * @author: yan
 * @since: 2021/6/22 15:49
 */
@Data
public class ChapterVo {
    private String id;
    private String title;

    private List<VideoVo> children = new ArrayList<>();
}

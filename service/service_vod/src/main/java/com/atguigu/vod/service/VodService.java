package com.atguigu.vod.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * TODO
 *
 * @ClassName: VodService
 * @author: yan
 * @since: 7/1/2021 1:18 PM
 */
public interface VodService {
    //上传视频到阿里云
    String uploadVideoAly(MultipartFile file);
}

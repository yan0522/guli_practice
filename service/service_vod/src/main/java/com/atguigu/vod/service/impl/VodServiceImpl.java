package com.atguigu.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.atguigu.vod.service.VodService;
import com.atguigu.vod.utils.ConstantVodUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;


/**
 * TODO
 *
 * @ClassName: VodServiceImpl
 * @author: yan
 * @since: 7/1/2021 1:18 PM
 */
@Service
public class VodServiceImpl implements VodService {

    //上传视频到阿里云
    @Override
    public String uploadVideoAly(MultipartFile file) {

        try {
            String fileName = file.getOriginalFilename();
            String title = fileName.substring(0,fileName.lastIndexOf("."));
            InputStream inputStream = file.getInputStream();

            UploadStreamRequest request = new UploadStreamRequest(ConstantVodUtils.ACCESS_KEY_ID,
                    ConstantVodUtils.ACCESS_KEY_SECRET,
                    title,fileName, inputStream);

            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);

            String videoId = null;
            if (response.isSuccess()) {
                videoId = response.getVideoId();
            }else {
                videoId = response.getVideoId();
            }
            return videoId;

        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

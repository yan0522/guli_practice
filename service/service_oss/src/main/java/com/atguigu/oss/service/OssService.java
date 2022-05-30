package com.atguigu.oss.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * TODO
 *
 * @InterfaceName: OssService
 * @author: yan
 * @since: 2021/6/16 16:56
 */
public interface OssService {
    //获取上传文件,返回文件的url
    String uploadFileAvatar(MultipartFile file);
}

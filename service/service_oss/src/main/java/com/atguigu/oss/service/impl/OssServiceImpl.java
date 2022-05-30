package com.atguigu.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.atguigu.oss.service.OssService;
import com.atguigu.oss.utils.ConstantPropertiesUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

/**
 * TODO
 *
 * @ClassName: OssServiceImpl
 * @author: yan
 * @since: 2021/6/16 16:56
 */
@Service
public class OssServiceImpl implements OssService {
    @Override
    public String uploadFileAvatar(MultipartFile file) {

        String endpoint = ConstantPropertiesUtils.END_POINT;
        String accessKeyId = ConstantPropertiesUtils.KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.KEY_SECRET;
        String bucketName = ConstantPropertiesUtils.BUCKETNAME;

        try {
            //获取文件名称
            String fileName = file.getOriginalFilename();
            //文件名拼接随机数防同名覆盖
            String uuid = UUID.randomUUID().toString().replaceAll("-","");
            fileName = uuid + fileName;
            //按日期建立文件夹存储
            String dataPath = new DateTime().toString("yyyy/MM/dd");

            fileName = dataPath + "/" + fileName;
            //创建oss对象
            OSS ossClient = new OSSClientBuilder().build(endpoint,accessKeyId,accessKeySecret);
            //获取上传文件的输入流
            InputStream inputStream = file.getInputStream();

            //调用oss方法实现上传
            ossClient.putObject(bucketName,fileName,inputStream);
            //关闭ossClient
            ossClient.shutdown();
            //把上传之后文件的路径返回
            //需要手动拼接
            String url = "http://"+bucketName+"."+endpoint+"/"+fileName;
            return url;

        }catch(Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}

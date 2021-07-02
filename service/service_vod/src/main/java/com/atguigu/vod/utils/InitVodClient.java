package com.atguigu.vod.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.profile.DefaultProfile;

/**
 * TODO
 *
 * @ClassName: InitObject
 * @author: yan
 * @since: 7/2/2021 12:56 PM
 */
public class InitVodClient {

    public static DefaultAcsClient initVodClient(String accessKeyId,String accessKeySecret) throws Exception{
        String region = "cn-beijing";
        DefaultProfile profile = DefaultProfile.getProfile(region,accessKeyId,accessKeySecret);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        return client;
    }
}

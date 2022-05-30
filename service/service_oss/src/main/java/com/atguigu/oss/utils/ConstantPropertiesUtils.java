package com.atguigu.oss.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * TODO
 *
 * @ClassName: ConstantPropertiesUtils
 * @author: yan
 * @since: 2021/6/16 16:43
 */
//实现InitializingBean之后，项目一启动，spring加载之后，执行下面afterPropertiesSet()方法
@Component
public class ConstantPropertiesUtils implements InitializingBean {

    //读取配置文件内容
    @Value("${aliyun.oss.file.endpoint}")
    private String endpoint;

    @Value("${aliyun.oss.file.keyid}")
    private String keyid;

    @Value("${aliyun.oss.file.keysecret}")
    private String keysecret;

    @Value("${aliyun.oss.file.bucketname}")
    private String bucketname;

    //上面都是私有的，所以现在写一个公开静态常量，赋值给它
    public static String END_POINT;
    public static String KEY_ID;
    public static String KEY_SECRET;
    public static String BUCKETNAME;


    @Override
    public void afterPropertiesSet() throws Exception {

        END_POINT= endpoint;
        KEY_ID = keyid;
        KEY_SECRET = keysecret;
        BUCKETNAME = bucketname;

    }
}

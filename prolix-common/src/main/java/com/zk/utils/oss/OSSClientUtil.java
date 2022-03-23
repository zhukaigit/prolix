package com.zk.utils.oss;

import com.aliyun.openservices.ClientConfiguration;
import com.aliyun.openservices.oss.OSSClient;
import com.aliyun.openservices.oss.model.OSSObject;
import com.aliyun.openservices.oss.model.ObjectMetadata;
import com.aliyun.openservices.oss.model.PutObjectResult;
import com.zk.utils.AssertUtil;
import lombok.Getter;

import java.io.*;
import java.util.UUID;

public class OSSClientUtil {
    @Getter
    private static OSSClient OSS_CLIENT;
    @Getter
    private static String    DEFAULT_BUCKET_NAME;
    @Getter
    private static String    END_POINT;
    @Getter
    private static String    ACCESS_ID;
    @Getter
    private static String    ACCESS_KEY;

    public OSSClientUtil(String endpoint, String accessId, String accessKey, String defaultBucketName,
                         int maxConnections, int socketTimeout, int connectionTimeout, int maxErrorRetry) {
        ClientConfiguration config = new ClientConfiguration();
        config.setMaxConnections(maxConnections > 0 ? maxConnections : 512);
        config.setSocketTimeout(socketTimeout > 0 ? socketTimeout : 120 * 1000);
        config.setConnectionTimeout(connectionTimeout > 0 ? connectionTimeout : 5 * 1000);
        config.setMaxErrorRetry(maxErrorRetry > 0 ? maxErrorRetry : 3);
        OSS_CLIENT = new OSSClient(endpoint, accessId, accessKey, config);
        DEFAULT_BUCKET_NAME = defaultBucketName;
        END_POINT = endpoint;
        ACCESS_ID = accessId;
        ACCESS_KEY = accessKey;
    }

    public static OSSObject getObject(String key) {
        return getObject(DEFAULT_BUCKET_NAME, key);
    }

    public static void deleteObject(String key) {
        deleteObject(DEFAULT_BUCKET_NAME, key);
    }

    public static void upload(String key, InputStream in, long contentLength) {
        AssertUtil.assertTrue(contentLength >= 0, "contentLength入参不能小于0");
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(contentLength);
        OSS_CLIENT.putObject(DEFAULT_BUCKET_NAME, key, in, metadata);
    }

    public static long upload(String key, byte[] content) {
        AssertUtil.assertTrue(content != null, "content入参不能为空");
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(content.length);
        upload(DEFAULT_BUCKET_NAME, key, new ByteArrayInputStream(content), metadata);
        return content.length;
    }

    private static PutObjectResult upload(String bucketName, String key, InputStream in, ObjectMetadata metadata) {
        AssertUtil.notBlank(bucketName, "bucketName入参不能为空");
        AssertUtil.notBlank(key, "key入参不能为空");
        AssertUtil.assertTrue(in != null, "content入参不能为空");
        AssertUtil.assertTrue(metadata != null, "metadata入参不能为空");
        return OSS_CLIENT.putObject(bucketName, key, in, metadata);

    }

    public static OSSObject getObject(String bucketName, String key) {
        AssertUtil.notBlank(bucketName, "bucketName入参不能为空");
        AssertUtil.notBlank(key, "key入参不能为空");
        return OSS_CLIENT.getObject(bucketName, key);
    }

    public static void deleteObject(String bucketName, String key) {
        AssertUtil.notBlank(bucketName, "bucketName入参不能为空");
        AssertUtil.notBlank(key, "key入参不能为空");
        OSS_CLIENT.deleteObject(bucketName, key);
    }

    public static String assembleOssUrl(String ossKey) {
        return assembleOssUrl(DEFAULT_BUCKET_NAME, END_POINT, ossKey);
    }

    public static String assembleOssUrl(String bucketName, String ossKey) {
        return assembleOssUrl(bucketName, END_POINT, ossKey);
    }

    /**
     * 组装ossUrl
     * 注意：可参考官方文档：https://help.aliyun.com/document_detail/31834.html?spm=a2c4g.11186623.6.628.76a82d56rEpT4Q
     *
     * @param bucketName 如：dzbd-test
     * @param endPoint 如：http://oss-cn-hzjbp-a-internal.aliyuncs.com
     * @param ossKey 如：0a570d2d-254e-4427-ba80-cc27273fca62-ossUrl.png
     * @return 如：http://dzbd-test.oss-cn-hzjbp-a-internal.aliyuncs.com/0a570d2d-254e-4427-ba80-cc27273fca62-ossUrl.png
     */
    private static String assembleOssUrl(String bucketName, String endPoint, String ossKey) {
        return String.format("http://%s.%s/%s", bucketName, endPoint.substring(7, endPoint.length()), ossKey);
    }


    public static void main (String[] args) throws IOException {
        testInit();

        String bucketName = "真实填写";
        File file = new File("E:\\10 - temp\\ossUrl.png");
        String ossKey = UUID.randomUUID().toString();

        long length = file.length();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(length);
        upload(bucketName, ossKey, new FileInputStream(file), metadata);

        String ossUrl = String.format("http://%s.%s/%s", bucketName, END_POINT.substring(7, END_POINT.length()),  ossKey);
        System.out.println("ossUrl=" + ossUrl);
    }

    private static void testInit () {
        String endpoint = "";
        String accessId = "";
        String accessKey = "";
        String defaultBucketName = "";
        int maxConnections = 500;
        int socketTimeout = 60 * 1000;
        int connectionTimeout =53000;
        int maxErrorRetry = 3;
        OSSClientUtil ossClientUtil = new OSSClientUtil(endpoint,accessId,accessKey,defaultBucketName,maxConnections,
                socketTimeout,connectionTimeout,maxErrorRetry);
    }
}

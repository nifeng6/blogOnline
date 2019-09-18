package net.codetip.blog.dto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TencentConfig {
    @Value("${tencent.cloud.accessKey}")
    private String accessKey;
    @Value("${tencent.cloud.secretKey}")
    private String secretKey;
    @Value("${tencent.cloud.bucket}")
    private String bucket;
    @Value("${tencent.cloud.bucketName}")
    private String bucketName;
    @Value("${tencent.cloud.path}")
    private String path;
    @Value("${tencent.cloud.prefix}")
    private String prefix;

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}

package com.gyo.tools.aws.cli.model;

import software.amazon.awssdk.services.s3.model.AccelerateConfiguration;
import software.amazon.awssdk.services.s3.model.AnalyticsConfiguration;

public class S3Bucket {

    private AccelerateConfiguration accelerateConfiguration;
    private String bucketName;
    private S3BucketPolicy bucketPolicy;
    private AnalyticsConfiguration bucketAnalyticsConfiguration;

    public void setAccelerateConfiguration(AccelerateConfiguration accelerateConfiguration) {
        this.accelerateConfiguration = accelerateConfiguration;
    }

    public AccelerateConfiguration getAccelerateConfiguration() {
        return accelerateConfiguration;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketPolicy(S3BucketPolicy bucketPolicy) {
        this.bucketPolicy = bucketPolicy;
    }

    public S3BucketPolicy getBucketPolicy() {
        return bucketPolicy;
    }

    public void setBucketAnalyticsConfiguration(AnalyticsConfiguration bucketAnalyticsConfiguration) {
        this.bucketAnalyticsConfiguration = bucketAnalyticsConfiguration;
    }

    public AnalyticsConfiguration getBucketAnalyticsConfiguration() {
        return bucketAnalyticsConfiguration;
    }
}

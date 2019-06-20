package com.gyo.tools.aws.cli.service;

import com.gyo.tools.aws.cli.model.CliProfileHolder;
import com.gyo.tools.aws.cli.model.S3Bucket;
import com.gyo.tools.aws.cli.model.S3BucketPolicy;
import com.gyo.tools.aws.cli.util.PrintUtils;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class S3Service extends AwsSdkClientAware<S3Client> implements ServiceDescriber<S3Bucket>{
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofLocalizedDateTime( FormatStyle.SHORT )
                    .withZone( ZoneId.systemDefault() );
    private static final Pattern BUCKET_PREFIX_PATTERN = Pattern.compile("(.*?)/(.*)");

    @Override
    S3Client buildClient() {
        return S3Client.builder()
                .credentialsProvider(ProfileCredentialsProvider.create(CliProfileHolder.instance().getAwsProfile()))
                .build();
    }

    public void listBuckets() {
        PrintUtils.printSuccess("\nLISTING BUCKETS FOR " + CliProfileHolder.instance().getAwsProfile() + " PROFILE ");
        PrintUtils.printSuccess("----");
        getClient().listBuckets().buckets()
                .forEach(bucket ->
                    PrintUtils.printSuccess("\t" + bucket.name() + " (" + DATE_FORMATTER.format(bucket.creationDate()) + ")")
                );
        PrintUtils.printSuccess("----");
    }

    public void listBucketContent(String bucketName) {
        if (bucketName == null || bucketName.isBlank()) {
            listBuckets();
        } else {
            try {
                getClient().listObjectsV2(createS3ListObjectRequest(bucketName))
                        .contents()
                        .forEach(item -> {
                            String fileSize = item.key().endsWith("/") ? "" : " - Size: " + item.size();
                            PrintUtils.printSuccess("\t" + item.key() + fileSize);
                        });
            } catch (Exception e) {
                if (e instanceof S3Exception) {
                    PrintUtils.printError("Bucket not found!");
                } else {
                    PrintUtils.printError(e.getMessage());
                }
            }
        }
    }

    public void upload(String bucketName, String fileName) {
        try {
            getClient().putObject(createUploadRequest(bucketName), Path.of(fileName));
            PrintUtils.printSuccess("Upload done.");
        } catch(Exception e) {
            if (e instanceof S3Exception) {
                PrintUtils.printError("Upload failed!");
            }
            throw e;
        }
    }

    public void delete(String bucketAndKey) {
        BucketAndPrefix bucketAndPrefix = extractBucketAndPrefix(bucketAndKey);
        if (bucketAndPrefix.containsPrefix()) {
            deleteFile(bucketAndPrefix);
        } else {
            deleteBucket(bucketAndPrefix.bucket);
        }
    }

    private void deleteBucket(String bucket) {
        DeleteBucketRequest deleteBucketRequest = DeleteBucketRequest.builder().bucket(bucket).build();
        try {
            getClient().deleteBucket(deleteBucketRequest);
            PrintUtils.printSuccess("Bucket deleted.");
        } catch (Exception e) {
            if (e instanceof S3Exception) {
                PrintUtils.printError("Delete failed!");
            }
            throw e;
        }
    }

    private void deleteFile(BucketAndPrefix bucketAndPrefix) {
        DeleteObjectRequest req = DeleteObjectRequest.builder()
                .bucket(bucketAndPrefix.bucket)
                .key(bucketAndPrefix.prefix)
                .build();
        try {
            getClient().deleteObject(req);
            PrintUtils.printSuccess("File deleted.");
        } catch (Exception e) {
            if (e instanceof S3Exception) {
                PrintUtils.printError("Delete failed!");
            }
            throw e;
        }
    }

    public void createBucket(String bucket) {
        BucketAndPrefix bucketAndPrefix = extractBucketAndPrefix(bucket);
        CreateBucketRequest createBucketRequest = CreateBucketRequest
                .builder().bucket(bucketAndPrefix.bucket).build();
        try {
            getClient().createBucket(createBucketRequest);
            PrintUtils.printSuccess("Bucket created.");
            if (bucketAndPrefix.containsPrefix()) {
                PrintUtils.printWarning("You specified a key and it was ignored.");
            }
        } catch(Exception e) {
            if (e instanceof S3Exception) {
                PrintUtils.printError("Delete failed!");
            }
            throw e;
        }
    }

    public void download(String bucketNameAndKey, String destination) {
        try {
            getClient().getObject(createGetObjectRequest(bucketNameAndKey), Paths.get(destination));
        } catch(Exception e) {
            if (e instanceof S3Exception) {
                PrintUtils.printError("Download failed!");
            }
            throw e;
        }
    }

    public S3Bucket describe(String bucketName) {
        Bucket bucket = getClient().listBuckets().buckets().stream()
                .filter(b -> b.name().equals(bucketName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Bucket "+bucketName+" not found"));

        S3Bucket s3Bucket = new S3Bucket();
        s3Bucket.setBucketName(bucketName);

        GetBucketAccelerateConfigurationResponse accelerateConfigurationResponse =
                getClient().getBucketAccelerateConfiguration(GetBucketAccelerateConfigurationRequest.builder().bucket(bucketName).build());
        if (accelerateConfigurationResponse.status() != null) {
            s3Bucket.setAccelerateConfiguration(AccelerateConfiguration.builder().status(accelerateConfigurationResponse.status()).build());
        }

        try {
            GetBucketPolicyResponse bucketPolicy = getClient().getBucketPolicy(GetBucketPolicyRequest.builder().bucket(bucketName).build());
            S3BucketPolicy s3BucketPolicy = new S3BucketPolicy();
            s3BucketPolicy.setPolicy(bucketPolicy.policy());
            s3Bucket.setBucketPolicy(s3BucketPolicy);
        } catch(Exception e) {

        }

        return s3Bucket;
    }

    private static GetObjectRequest createGetObjectRequest(String bucketNameAndKey) {
        BucketAndPrefix bucketAndPrefix = extractBucketAndPrefix(bucketNameAndKey);
        GetObjectRequest.Builder reqBuilder = GetObjectRequest.builder()
                .bucket(bucketAndPrefix.bucket);
        if (bucketAndPrefix.containsPrefix()) {
            reqBuilder.key(bucketAndPrefix.prefix);
        }
        return reqBuilder.build();
    }

    private static CreateBucketRequest createCreateBucketRequest(String bucket) {
        BucketAndPrefix bucketAndPrefix = extractBucketAndPrefix(bucket);
        return CreateBucketRequest
                .builder().bucket(bucketAndPrefix.bucket).build();
    }

    private static ListObjectsV2Request createS3ListObjectRequest(String bucketAndKey) {
        BucketAndPrefix bucketAndPrefix = extractBucketAndPrefix(bucketAndKey);
        ListObjectsV2Request.Builder reqBuilder = ListObjectsV2Request.builder()
                .bucket(bucketAndPrefix.bucket);
        if (bucketAndPrefix.containsPrefix()) {
            reqBuilder.prefix(bucketAndPrefix.prefix);
        }
        return reqBuilder.build();
    }

    private static PutObjectRequest createUploadRequest(String bucketAndKey) {
        BucketAndPrefix bucketAndPrefix = extractBucketAndPrefix(bucketAndKey);
        PutObjectRequest.Builder reqBuilder = PutObjectRequest.builder()
                .bucket(bucketAndPrefix.bucket);
        if (bucketAndPrefix.containsPrefix()) {
            reqBuilder.key(bucketAndPrefix.prefix);
        }
        return reqBuilder.build();
    }

    private static BucketAndPrefix extractBucketAndPrefix(String bucketAndKey) {
        BucketAndPrefix bp = new BucketAndPrefix();
        bp.bucket = bucketAndKey;
        if (bucketAndKey.contains("/")) {
            Matcher matcher = BUCKET_PREFIX_PATTERN.matcher(bucketAndKey);
            if (matcher.find()) {
                bp.bucket = matcher.group(1);
                bp.prefix = matcher.group(2);
            }
        }
        return bp;
    }

    private static class BucketAndPrefix {
        String bucket;
        String prefix;

        boolean containsPrefix() {
            return prefix != null && !prefix.isBlank();
        }
    }
}

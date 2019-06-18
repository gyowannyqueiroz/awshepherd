package com.gyo.tools.aws.cli.service;

import com.gyo.tools.aws.cli.model.CliProfileHolder;
import com.gyo.tools.aws.cli.util.ShellUtils;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.nio.file.Path;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class S3Service {
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofLocalizedDateTime( FormatStyle.SHORT )
                    .withZone( ZoneId.systemDefault() );
    private static final Pattern BUCKET_PREFIX_PATTERN = Pattern.compile("(.*?)/(.*)");
    private S3Client s3Client;

    public S3Service() {
        reset();
    }

    public void reset() {
        buildS3Client();
    }

    public void listBuckets() {
        ShellUtils.printSuccess("\nLISTING BUCKETS FOR " + CliProfileHolder.getAwsProfile() + " PROFILE ");
        ShellUtils.printSuccess("----");
        s3Client.listBuckets().buckets()
                .forEach(bucket ->
                    ShellUtils.printSuccess("\t" + bucket.name() + " (" + DATE_FORMATTER.format(bucket.creationDate()) + ")")
                );
        ShellUtils.printSuccess("----");
    }

    public void listBucketContent(String bucketName) {
        if (bucketName == null || bucketName.isBlank()) {
            listBuckets();
        } else {
            try {
                s3Client.listObjectsV2(createS3ListObjectRequest(bucketName))
                        .contents()
                        .forEach(item -> {
                            String fileSize = item.key().endsWith("/") ? "" : " - Size: " + item.size();
                            ShellUtils.printSuccess("\t" + item.key() + fileSize);
                        });
            } catch (Exception e) {
                if (e instanceof S3Exception) {
                    ShellUtils.printError("Bucket not found!");
                } else {
                    ShellUtils.printError(e.getMessage());
                }
            }
        }
    }

    public void upload(String bucketName, String fileName) {
        try {
            s3Client.putObject(createUploadRequest(bucketName), Path.of(fileName));
            ShellUtils.printSuccess("Upload done.");
        } catch(Exception e) {
            if (e instanceof S3Exception) {
                ShellUtils.printError("Upload failed!");
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
            s3Client.deleteBucket(deleteBucketRequest);
            ShellUtils.printSuccess("Bucket deleted.");
        } catch (Exception e) {
            if (e instanceof S3Exception) {
                ShellUtils.printError("Delete failed!");
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
            s3Client.deleteObject(req);
            ShellUtils.printSuccess("File deleted.");
        } catch (Exception e) {
            if (e instanceof S3Exception) {
                ShellUtils.printError("Delete failed!");
            }
            throw e;
        }
    }

    public void createBucket(String bucket) {
        BucketAndPrefix bucketAndPrefix = extractBucketAndPrefix(bucket);
        CreateBucketRequest createBucketRequest = CreateBucketRequest
                .builder().bucket(bucketAndPrefix.bucket).build();
        try {
            s3Client.createBucket(createBucketRequest);
            ShellUtils.printSuccess("Bucket created.");
            if (bucketAndPrefix.containsPrefix()) {
                ShellUtils.printWarning("You specified a key and it was ignored.");
            }
        } catch(Exception e) {
            if (e instanceof S3Exception) {
                ShellUtils.printError("Delete failed!");
            }
            throw e;
        }
    }

    private void buildS3Client() {
        s3Client = S3Client.builder()
                .credentialsProvider(ProfileCredentialsProvider.create(CliProfileHolder.getAwsProfile()))
                .build();
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

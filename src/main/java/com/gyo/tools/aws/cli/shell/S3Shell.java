package com.gyo.tools.aws.cli.shell;

import com.gyo.tools.aws.cli.event.ProfileChangedEvent;
import com.gyo.tools.aws.cli.service.S3Service;
import com.gyo.tools.aws.cli.util.PrintUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import static com.gyo.tools.aws.cli.util.PlaceHolderUtils.formatStringWithEnvVariables;

@ShellComponent("S3 Shell")
public class S3Shell implements ApplicationListener<ProfileChangedEvent> {

    @Autowired
    private S3Service s3Service;

    @ShellMethod(key = "s3-ls", value = "List buckets and bucket contents")
    public void list(@ShellOption(defaultValue = "",
            help="The bucket you want to see the content or omit it to list all buckets") String bucketName) {
        s3Service.listBucketContent(bucketName);
    }

    @ShellMethod(key = "s3-push", value = "S3 upload file operation")
    public void upload(@ShellOption(help="Destination bucket") String bucketName,
                         @ShellOption(help="File to be uploaded") String fileName) {
        PrintUtils.printSuccess("Pushing...");
        s3Service.upload(bucketName, fileName);
    }

    @ShellMethod(key = "s3-pull", value = "S3 download file operation")
    public void download(@ShellOption(help="Destination bucket") String bucketNameAndKey, String destinationPath) {
        PrintUtils.printSuccess("Pulling...");
        s3Service.download(bucketNameAndKey, formatStringWithEnvVariables(destinationPath));
    }

    @ShellMethod(key = "s3-rm", value = "S3 delete file")
    public void delete(@ShellOption(help="File to delete") String bucketAndKey) {
        PrintUtils.printSuccess("Deleting...");
        s3Service.delete(bucketAndKey);
    }

    @ShellMethod(key = "s3-create", value = "Creates a bucket with the specified name")
    public void create(String bucket) {
        PrintUtils.printSuccess("Creating bucket...");
        s3Service.createBucket(bucket);
    }

    private void resetServices() {
        s3Service.reset();
    }

    @Override
    public void onApplicationEvent(ProfileChangedEvent event) {
        resetServices();
    }
}

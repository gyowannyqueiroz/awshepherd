package com.gyo.tools.aws.cli.shell;

import com.gyo.tools.aws.cli.event.ProfileChangedEvent;
import com.gyo.tools.aws.cli.model.CliProfileHolder;
import com.gyo.tools.aws.cli.model.History;
import com.gyo.tools.aws.cli.service.S3Service;
import com.gyo.tools.aws.cli.util.ShellUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class S3Shell implements ApplicationListener<ProfileChangedEvent> {

    @Autowired
    private S3Service s3Service;

    @ShellMethod(key = "s3-ls", value = "List buckets and bucket contents")
    public void s3List(@ShellOption(defaultValue = "",
            help="The bucket you want to see the content or omit it to list all buckets") String bucketName) {
        History.add("s3-ls");
        s3Service.listBucketContent(bucketName);
    }

    @ShellMethod(key = "s3-up", value = "S3 upload file operations")
    public void s3Upload(@ShellOption(help="Destination bucket") String bucketName,
                         @ShellOption(help="File to be uploaded") String fileName) {
        History.add("s3-up");
        ShellUtils.printSuccess("Uploading...");
        s3Service.upload(bucketName, fileName);
    }

    @ShellMethod(key = "s3-rm", value = "S3 delete file")
    public void s3Delete(@ShellOption(help="File to delete") String bucketAndKey) {
        History.add("s3-rm");
        ShellUtils.printSuccess("Deleting...");
        s3Service.delete(bucketAndKey);
    }

    private void resetServices() {
        s3Service.reset();
    }

    @Override
    public void onApplicationEvent(ProfileChangedEvent event) {
        resetServices();
    }
}
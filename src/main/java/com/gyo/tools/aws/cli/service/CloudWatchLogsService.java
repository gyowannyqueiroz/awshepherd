package com.gyo.tools.aws.cli.service;

import com.gyo.tools.aws.cli.model.CliProfileHolder;
import com.gyo.tools.aws.cli.translator.SingleColumnTableModelTranslator;
import com.gyo.tools.aws.cli.util.PrintUtils;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.services.cloudwatchlogs.CloudWatchLogsClient;
import software.amazon.awssdk.services.cloudwatchlogs.model.LogGroup;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CloudWatchLogsService implements AwsServiceAware {

    private CloudWatchLogsClient cloudWathLogsClient;

    public CloudWatchLogsService() {
        reset();
    }

    @Override
    public void reset() {
        buildCloudWatchClient();
    }

    public void listLogGroups() {
        List<String> logGroupNames = cloudWathLogsClient.describeLogGroups()
                .logGroups().stream()
                .map(LogGroup::logGroupName)
                .collect(Collectors.toList());
        PrintUtils.printTable(new SingleColumnTableModelTranslator("Log Groups", logGroupNames).translate());
    }

    private void buildCloudWatchClient() {
        cloudWathLogsClient = CloudWatchLogsClient.builder()
                .credentialsProvider(ProfileCredentialsProvider.create(CliProfileHolder.getAwsProfile()))
                .build();
    }

}

package com.gyo.tools.aws.cli.service;

import com.gyo.tools.aws.cli.model.CliProfileHolder;
import com.gyo.tools.aws.cli.translator.CloudWatchLogsTableModelTranslator;
import com.gyo.tools.aws.cli.translator.SingleColumnTableModelTranslator;
import com.gyo.tools.aws.cli.util.PrintUtils;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.services.cloudwatchlogs.CloudWatchLogsClient;
import software.amazon.awssdk.services.cloudwatchlogs.model.*;

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
        PrintUtils.printClassicTable(new SingleColumnTableModelTranslator("Log Groups", logGroupNames).translate());
    }

    public void listLogsByGroupName(String logGroupName, int limit) {
        LogStream latestLogStream = getLatestLogStream(logGroupName);
        if (latestLogStream == null) {
            PrintUtils.printWarning("No events found.");
            return;
        }
        GetLogEventsRequest req = GetLogEventsRequest.builder()
                .logGroupName(logGroupName)
                .logStreamName(latestLogStream.logStreamName())
                .limit(limit)
                .build();
        List<OutputLogEvent> events = cloudWathLogsClient.getLogEvents(req).events();
        PrintUtils.printNoBorderTable(new CloudWatchLogsTableModelTranslator(events).translate());
    }

    private LogStream getLatestLogStream(String logGroupName) {
        DescribeLogStreamsRequest req = DescribeLogStreamsRequest.builder()
                .logGroupName(logGroupName)
                .orderBy(OrderBy.LAST_EVENT_TIME)
                .build();
        List<LogStream> logStreams = cloudWathLogsClient.describeLogStreams(req).logStreams();
        return logStreams.get(logStreams.size()-1);
    }

    private void buildCloudWatchClient() {
        cloudWathLogsClient = CloudWatchLogsClient.builder()
                .credentialsProvider(ProfileCredentialsProvider.create(CliProfileHolder.getAwsProfile()))
                .build();
    }
}

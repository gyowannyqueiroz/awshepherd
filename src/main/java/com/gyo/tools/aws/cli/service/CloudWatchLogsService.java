package com.gyo.tools.aws.cli.service;

import com.gyo.tools.aws.cli.model.CliEnvironment;
import com.gyo.tools.aws.cli.translator.CloudWatchLogGroupsTableModelTranslator;
import com.gyo.tools.aws.cli.translator.CloudWatchLogsTableModelTranslator;
import com.gyo.tools.aws.cli.util.PrintUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.services.cloudwatchlogs.CloudWatchLogsClient;
import software.amazon.awssdk.services.cloudwatchlogs.model.*;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CloudWatchLogsService extends AwsSdkClientAware<CloudWatchLogsClient> {

    @Autowired
    public CloudWatchLogsService(CliEnvironment cliEnvironment) {
        super(cliEnvironment);
    }

    @Override
    public CloudWatchLogsClient buildClient(CliEnvironment env) {
        return CloudWatchLogsClient.builder()
                .credentialsProvider(ProfileCredentialsProvider.create(env.getAwsProfile()))
                .build();
    }

    public void listLogGroups(String filterByName) {
        List<LogGroup> logGroups = getClient().describeLogGroups()
                .logGroups();
        List<HashMap.SimpleEntry<LogGroup, List<LogStream>>> map = logGroups.stream()
                .map(lg -> new HashMap.SimpleEntry<LogGroup, List<LogStream>>(lg, getLogStreamsByLogGroup(lg.logGroupName())))
                .collect(Collectors.toList());

        Stream<AbstractMap.SimpleEntry<LogGroup, List<LogStream>>> stream = map.stream();
        if (filterByName != null && !filterByName.isEmpty()) {
            stream = stream.filter(lg -> lg.getKey().logGroupName().startsWith(filterByName));
        }
        List<AbstractMap.SimpleEntry<LogGroup, List<LogStream>>> groupsAndStreams = stream.collect(Collectors.toList());
        PrintUtils.printClassicTable(new CloudWatchLogGroupsTableModelTranslator(groupsAndStreams).translate());
    }

    private List<LogStream> getLogStreamsByLogGroup(String logGroupName) {
        DescribeLogStreamsRequest req = DescribeLogStreamsRequest.builder()
                .logGroupName(logGroupName)
                .orderBy(OrderBy.LAST_EVENT_TIME)
                .build();
        return getClient().describeLogStreams(req).logStreams();
    }

    public void listLogsByGroupName(String logGroupName, String logStream, int limit) {
        if (logStream == null || logStream.isEmpty()) {
            LogStream latestLogStream = getLatestLogStream(logGroupName);
            if (latestLogStream == null) {
                PrintUtils.printWarning("No events found.");
                return;
            }
            logStream = latestLogStream.logStreamName();
        }
        GetLogEventsRequest req = GetLogEventsRequest.builder()
                .logGroupName(logGroupName)
                .logStreamName(logStream)
                .limit(limit)
                .build();
        List<OutputLogEvent> events = getClient().getLogEvents(req).events();
        PrintUtils.printNoBorderTable(new CloudWatchLogsTableModelTranslator(events).translate());
    }

    private LogStream getLatestLogStream(String logGroupName) {
        List<LogStream> logStreams = getLogStreamsByLogGroup(logGroupName);
        return logStreams.get(logStreams.size()-1);
    }

}

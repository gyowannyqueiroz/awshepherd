package com.gyo.tools.aws.cli.translator;

import software.amazon.awssdk.services.cloudwatchlogs.model.LogGroup;
import software.amazon.awssdk.services.cloudwatchlogs.model.LogStream;

import java.time.Instant;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class CloudWatchLogGroupsTableModelTranslator
        extends TableModelTranslator<HashMap.SimpleEntry<LogGroup, List<LogStream>>>{
    private static final String[] COL_NAMES = {"Log Group", "Log Streams"};

    public CloudWatchLogGroupsTableModelTranslator(
            List<AbstractMap.SimpleEntry<LogGroup,
            List<LogStream>>> values) {
        super(values);
    }

    @Override
    String[] translateRow(AbstractMap.SimpleEntry<LogGroup, List<LogStream>> o) {
        return List.of(
                o.getKey().logGroupName(),
                joinLogStreamsNameAndCreationDate(o.getValue())
        ).toArray(new String[]{});
    }

    private String joinLogStreamsNameAndCreationDate(List<LogStream> logStreams) {
        return logStreams.stream()
                .map(ls -> ls.logStreamName() + " [" + formatLocalDateTimeFromInstant(Instant.ofEpochMilli(ls.creationTime())) +"]")
                .collect(Collectors.joining("\n"));
    }

    @Override
    String[] getColumnNames() {
        return COL_NAMES;
    }
}

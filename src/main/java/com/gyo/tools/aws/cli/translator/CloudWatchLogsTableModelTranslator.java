package com.gyo.tools.aws.cli.translator;

import software.amazon.awssdk.services.cloudwatchlogs.model.OutputLogEvent;

import java.util.List;

public class CloudWatchLogsTableModelTranslator extends TableModelTranslator<OutputLogEvent>{
    private static final String[] COL_NAMES = {"Message"};

    public CloudWatchLogsTableModelTranslator(List<OutputLogEvent> values) {
        super(values);
    }

    @Override
    String[] translateRow(OutputLogEvent o) {
        return List.of(
                o.message()
        ).toArray(new String[]{});
    }

    @Override
    String[] getColumnNames() {
        return COL_NAMES;
    }
}

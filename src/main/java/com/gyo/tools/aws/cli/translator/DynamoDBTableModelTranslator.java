package com.gyo.tools.aws.cli.translator;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DynamoDBTableModelTranslator extends TableModelTranslator<Map<String, AttributeValue>> {

    private String[] colNames;

    public DynamoDBTableModelTranslator(List<Map<String, AttributeValue>> values) {
        super(values);
    }

    @Override
    String[] translateRow(Map<String, AttributeValue> value) {
        String[] row = new String[getColumnNames().length];
        int colIndex = 0;
        for (String col: value.keySet()) {
            row[colIndex++] = getStringFromDynamoDBValue(value.get(col));
        }
        return row;
    }

    @Override
    String[] getColumnNames() {
        if (colNames == null) {
            colNames = getValues().get(0).keySet().stream()
                    .map(String::toUpperCase)
                    .collect(Collectors.toList())
                    .toArray(new String[]{});
        }
        return colNames;
    }

    private String getStringFromDynamoDBValue(AttributeValue colValue) {
        if (colValue != null) {
            if (colValue.s() != null && !colValue.s().isEmpty()) {
                return colValue.s();
            }
            if (colValue.n() != null && !colValue.n().isEmpty()) {
                return colValue.n();
            }
            if (colValue.bool() != null) {
                return colValue.bool().toString();
            }
            if (colValue.ns() != null) {
                return colValue.l().stream().map(AttributeValue::n).collect(Collectors.joining(","));
            }
            if (colValue.ss() != null) {
                return colValue.l().stream().map(AttributeValue::s).collect(Collectors.joining(","));
            }
        }
        return "";
    }
}

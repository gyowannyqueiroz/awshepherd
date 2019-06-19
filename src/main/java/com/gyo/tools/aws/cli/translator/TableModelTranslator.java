package com.gyo.tools.aws.cli.translator;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

public abstract class TableModelTranslator<T> {

    private final List<T> values;

    public TableModelTranslator(List<T> values) {
        this.values = values;
    }

    public String[][] translate() {
        String[][] data = new String[values.size()+1][getColumnNames().length];
        data[0] = getColumnNames();
        int rowIndex = 1;
        for (T value: values) {
            data[rowIndex++] = translateRow(value);
        }
        return data;
    }

    List<T> getValues() {
        return values;
    }

    protected String formatLocalDateTimeFromInstance(Instant instant) {
        if (instant == null) {
            return "";
        }
        return LocalDateTime.ofInstant(instant, ZoneOffset.systemDefault())
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    protected String formatLocalDateFromInstance(Instant instant) {
        if (instant == null) {
            return "";
        }
        return LocalDate.ofInstant(instant, ZoneOffset.systemDefault())
                .format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    abstract String[] translateRow(T value);

    abstract String[] getColumnNames();

}

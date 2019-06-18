package com.gyo.tools.aws.cli.translator;

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

    abstract String[] translateRow(T value);

    abstract String[] getColumnNames();

}

package com.gyo.tools.aws.cli.translator;

import java.util.List;

public class SingleColumnTableModelTranslator extends TableModelTranslator<String> {

    private final String[] columnName;

    public SingleColumnTableModelTranslator(String columnName, List<String> values) {
        super(values);
        this.columnName = new String[]{columnName};
    }

    @Override
    String[] translateRow(String value) {
        return new String[]{value};
    }

    @Override
    String[] getColumnNames() {
        return columnName;
    }
}

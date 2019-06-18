package com.gyo.tools.aws.cli.model;

import org.springframework.shell.table.TableModel;

public class DynamoDBTableModel extends TableModel {
    @Override
    public int getRowCount() {
        return 0;
    }

    @Override
    public int getColumnCount() {
        return 0;
    }

    @Override
    public Object getValue(int row, int column) {
        return null;
    }
}

package com.gyo.tools.aws.cli.util;

import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.shell.table.ArrayTableModel;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.Table;
import org.springframework.shell.table.TableBuilder;

public final class PrintUtils {

    public static String ansiSuccessString(String message) {
        return AnsiOutput.toString(AnsiColor.GREEN, message, AnsiColor.DEFAULT);
    }

    public static void printSuccess(String message) {
        System.out.println(ansiSuccessString(message));
    }

    public static void printError(String message) {
        System.out.println(AnsiOutput.toString(
                AnsiColor.RED, message, AnsiColor.DEFAULT));
    }

    public static void printWarning(String message) {
        System.out.println(AnsiOutput.toString(
                AnsiColor.YELLOW, message, AnsiColor.DEFAULT));
    }

    public static void printTable(String[][] data) {
        Table table = new TableBuilder(new ArrayTableModel(data))
                .addFullBorder(BorderStyle.fancy_light)
                .build();
        PrintUtils.printSuccess(table.render(200));
    }
}

package com.gyo.tools.aws.cli.util;

import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;

public final class ShellUtils {

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
}

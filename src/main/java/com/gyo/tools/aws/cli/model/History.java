package com.gyo.tools.aws.cli.model;

import java.util.ArrayList;
import java.util.List;

public final class History {
    public static final List<String> COMMAND_HISTORY = new ArrayList<>();

    public static void add(String command) {
        COMMAND_HISTORY.add(command);
    }

    public static List<String> getCommandHistory() {
        return List.copyOf(COMMAND_HISTORY);
    }
}

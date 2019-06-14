package com.gyo.tools.aws.cli.shell;

import com.gyo.tools.aws.cli.model.History;
import com.gyo.tools.aws.cli.util.ShellUtils;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class HistoryShell {

    @ShellMethod(key="history", value="Prints command history")
    public void history() {
        listHistory();
    }

    private void listHistory() {
        int index = 0;
        for (String c: History.getCommandHistory()) {
            ShellUtils.printWarning(index + " " + c);
            index++;
        }
    }
}

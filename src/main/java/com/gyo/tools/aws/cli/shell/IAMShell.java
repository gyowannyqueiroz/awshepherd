package com.gyo.tools.aws.cli.shell;

import com.gyo.tools.aws.cli.service.IamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class IAMShell extends AbstractShell {

    @Autowired
    private IamService iamService;

    @ShellMethod(key = "iam-ls", value = "Lists all the IAM users")
    public void listUsers() {
        iamService.listUsers();
    }

    @Override
    void resetServices() {
        iamService.reset();
    }
}

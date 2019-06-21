package com.gyo.tools.aws.cli.shell;

import com.gyo.tools.aws.cli.model.CliEnvironment;
import com.gyo.tools.aws.cli.service.IamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class IAMShell extends AbstractShell {

    private IamService iamService;

    @Autowired
    public IAMShell(IamService iamService, CliEnvironment cliEnvironment) {
        super(cliEnvironment);
        this.iamService = iamService;
    }

    @ShellMethod(key = "iam-ls", value = "Lists all the IAM users")
    public void listUsers() {
        iamService.listUsers();
    }

    @Override
    void resetServices() {
        iamService.reset();
    }
}

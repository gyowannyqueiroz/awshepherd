package com.gyo.tools.aws.cli.shell;

import com.gyo.tools.aws.cli.model.CliEnvironment;
import com.gyo.tools.aws.cli.service.EC2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class EC2Shell extends AbstractShell {

    private EC2Service ec2Service;

    @Autowired
    public EC2Shell(EC2Service ec2Service, CliEnvironment cliEnvironment) {
        super(cliEnvironment);
        this.ec2Service = ec2Service;
    }

    @ShellMethod(key = "ec2-ls", value = "List EC2 instances")
    public void listInstances() {
        ec2Service.list();
    }

    @ShellMethod(key = "ec2-desc", value = "Describe EC2 instance details")
    public void describeInstance(String instanceId) {
        ec2Service.describe(instanceId);
    }

    @Override
    void resetServices() {
        ec2Service.reset();
    }
}

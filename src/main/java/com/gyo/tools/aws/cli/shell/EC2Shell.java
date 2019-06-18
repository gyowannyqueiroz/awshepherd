package com.gyo.tools.aws.cli.shell;

import com.gyo.tools.aws.cli.event.ProfileChangedEvent;
import com.gyo.tools.aws.cli.service.EC2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class EC2Shell implements ApplicationListener<ProfileChangedEvent> {

    @Autowired
    private EC2Service ec2Service;

    @ShellMethod(key = "ec2-ls", value = "List EC2 instances")
    public void listInstances() {
        ec2Service.list();
    }

    @ShellMethod(key = "ec2-desc", value = "Describe EC2 instance details")
    public void describeInstance(String instanceId) {
        ec2Service.describe(instanceId);
    }

    @Override
    public void onApplicationEvent(ProfileChangedEvent event) {
        resetServices();
    }

    private void resetServices() {
        ec2Service.reset();
    }
}

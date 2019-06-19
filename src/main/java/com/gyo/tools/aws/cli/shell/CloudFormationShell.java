package com.gyo.tools.aws.cli.shell;

import com.gyo.tools.aws.cli.service.CFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellMethod;

//@ShellComponent
public class CloudFormationShell {

    @Autowired
    private CFService cfService;

    @ShellMethod(key = "cf-gen", value = "Generates CF script for the specified service instance")
    public void generateCF(String service, String name) {
        if ("S3".equalsIgnoreCase(service)) {
            cfService.extractForS3Bucket(name);
        }
    }
}

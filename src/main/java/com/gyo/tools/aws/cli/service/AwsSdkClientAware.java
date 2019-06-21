package com.gyo.tools.aws.cli.service;

import com.gyo.tools.aws.cli.model.CliEnvironment;
import software.amazon.awssdk.core.SdkClient;

public abstract class AwsSdkClientAware<T extends SdkClient> {

    private T client;

    private CliEnvironment cliEnvironment;

    public AwsSdkClientAware(CliEnvironment cliEnvironment) {
        this.cliEnvironment = cliEnvironment;
        reset();
    }

    public CliEnvironment getCliEnvironment() {
        return cliEnvironment;
    }

    public void reset() {
        client = buildClient(cliEnvironment);
    }

    T getClient() {
        return client;
    }

    abstract T buildClient(CliEnvironment env);
}

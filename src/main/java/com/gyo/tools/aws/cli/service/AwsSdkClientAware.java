package com.gyo.tools.aws.cli.service;

import software.amazon.awssdk.core.SdkClient;

public abstract class AwsSdkClientAware<T extends SdkClient> {

    private T client;

    public AwsSdkClientAware() {
        reset();
    }

    public void reset() {
        client = buildClient();
    }

    T getClient() {
        return client;
    }

    abstract T buildClient();
}

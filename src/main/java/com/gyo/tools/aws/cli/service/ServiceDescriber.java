package com.gyo.tools.aws.cli.service;

public interface ServiceDescriber<T> {

    T describe(String name);
}

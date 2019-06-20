package com.gyo.tools.aws.cli.model;

import software.amazon.awssdk.profiles.ProfileFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class CliProfileHolder {
    private static CliProfileHolder instance;
    private String awsProfile = "default";
    private Set<String> awsProfiles;
    private static Map<String,String> ENV_MAP = new HashMap<>();

    public static CliProfileHolder instance() {
        if (instance == null) {
            instance = new CliProfileHolder();
        }
        return instance;
    }

    private CliProfileHolder(){
        loadAwsProfiles();
    }

    public String getAwsProfile() {
        return awsProfile;
    }

    public void setAwsProfile(String awsProfile) {
        this.awsProfile = awsProfile;
    }

    public Set<String> getAwsProfiles() {
        return awsProfiles;
    }

    public void putEnv(String key, String value) {
        ENV_MAP.put(key, value);
    }

    public Map<String,String> getEnvMap() {
        return Map.copyOf(ENV_MAP);
    }

    public void loadAwsProfiles() {
        awsProfiles = ProfileFile.defaultProfileFile().profiles().keySet();
    }
}

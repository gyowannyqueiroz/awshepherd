package com.gyo.tools.aws.cli.model;

import com.gyo.tools.aws.cli.util.PrintUtils;
import software.amazon.awssdk.profiles.ProfileFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CliProfileHolder {
    private static CliProfileHolder instance;
    private String awsProfile;
    private List<String> awsProfiles;
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

    public List<String> getAwsProfiles() {
        return awsProfiles;
    }

    public void putEnv(String key, String value) {
        ENV_MAP.put(key, value);
    }

    public Map<String,String> getEnvMap() {
        return Map.copyOf(ENV_MAP);
    }

    public void loadAwsProfiles() {
        awsProfiles = List.copyOf(ProfileFile.defaultProfileFile().profiles().keySet());
        if (awsProfile == null && !awsProfiles.isEmpty()) {
            awsProfile = awsProfiles.get(0);
        } else {
            PrintUtils.printWarning("There are no profiles configured in your credentials file");
        }
    }
}

package com.gyo.tools.aws.cli.model;

import software.amazon.awssdk.profiles.ProfileFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class CliProfileHolder {
    private static String awsProfile = "default";
    private static Set<String> awsProfiles = ProfileFile.defaultProfileFile().profiles().keySet();
    private static Map<String,String> ENV_MAP = new HashMap<>();

    public static String getAwsProfile() {
        return awsProfile;
    }

    public static void setAwsProfile(String awsProfile) {
        CliProfileHolder.awsProfile = awsProfile;
    }

    public static Set<String> getAwsProfiles() {
        return awsProfiles;
    }

    public static void putEnv(String key, String value) {
        ENV_MAP.put(key, value);
    }

    public static Map<String,String> getEnvMap() {
        return Map.copyOf(ENV_MAP);
    }
}

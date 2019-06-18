package com.gyo.tools.aws.cli.model;

import software.amazon.awssdk.profiles.ProfileFile;

import java.util.Set;

public final class CliProfileHolder {
    private static String awsProfile = "default";
    private static Set<String> awsProfiles = ProfileFile.defaultProfileFile().profiles().keySet();

    public static String getAwsProfile() {
        return awsProfile;
    }

    public static void setAwsProfile(String awsProfile) {
        CliProfileHolder.awsProfile = awsProfile;
    }

    public static Set<String> getAwsProfiles() {
        return awsProfiles;
    }

}

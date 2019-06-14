package com.gyo.tools.aws.cli.model;

public final class CliProfileHolder {
    private static String awsProfile = "default";

    public static String getAwsProfile() {
        return awsProfile;
    }

    public static void setAwsProfile(String awsProfile) {
        CliProfileHolder.awsProfile = awsProfile;
    }

}

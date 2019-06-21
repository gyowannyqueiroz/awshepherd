package com.gyo.tools.aws.cli.model;

import com.gyo.tools.aws.cli.util.PrintUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.profiles.ProfileFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Scope("singleton")
public class CliEnvironment {
    private String awsProfile;
    private List<String> awsProfiles;
    private Map<String,String> envVarsMap = new HashMap<>();

    public CliEnvironment(){
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

    public boolean profileExists(String profile) {
        return awsProfiles.contains(profile);
    }

    public void putEnv(String key, String value) {
        envVarsMap.put(key, value);
    }

    public Map<String,String> getEnvMap() {
        return Map.copyOf(envVarsMap);
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

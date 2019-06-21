package com.gyo.tools.aws.cli.service;

import com.gyo.tools.aws.cli.model.CliEnvironment;
import com.gyo.tools.aws.cli.translator.IamUserTableModelTranslator;
import com.gyo.tools.aws.cli.util.PrintUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.iam.IamClient;
import software.amazon.awssdk.services.iam.model.User;

import java.util.List;

@Service
public class IamService extends AwsSdkClientAware<IamClient> {

    @Autowired
    public IamService(CliEnvironment cliEnvironment) {
        super(cliEnvironment);
    }

    public void listUsers() {
        List<User> users = getClient().listUsers().users();
        PrintUtils.printClassicTable(new IamUserTableModelTranslator(users).translate());
    }

    @Override
    IamClient buildClient(CliEnvironment env) {
        return IamClient.builder()
                .credentialsProvider(ProfileCredentialsProvider.create(env.getAwsProfile()))
                .region(Region.AWS_GLOBAL)
                .build();
    }
}

package com.gyo.tools.aws.cli.service;

import com.gyo.tools.aws.cli.model.CliProfileHolder;
import com.gyo.tools.aws.cli.translator.IamUserTableModelTranslator;
import com.gyo.tools.aws.cli.util.PrintUtils;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.iam.IamClient;
import software.amazon.awssdk.services.iam.model.User;

import java.util.List;

@Service
public class IamService extends AwsSdkClientAware<IamClient> {

    public void listUsers() {
        List<User> users = getClient().listUsers().users();
        PrintUtils.printClassicTable(new IamUserTableModelTranslator(users).translate());
    }

    @Override
    IamClient buildClient() {
        return IamClient.builder()
                .credentialsProvider(ProfileCredentialsProvider.create(CliProfileHolder.instance().getAwsProfile()))
                .region(Region.AWS_GLOBAL)
                .build();
    }
}

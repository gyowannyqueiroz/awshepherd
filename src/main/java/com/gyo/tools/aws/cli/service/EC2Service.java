package com.gyo.tools.aws.cli.service;

import com.gyo.tools.aws.cli.model.CliEnvironment;
import com.gyo.tools.aws.cli.translator.EC2DetailsTableModelTranslator;
import com.gyo.tools.aws.cli.translator.EC2TableModelTranslator;
import com.gyo.tools.aws.cli.util.PrintUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesRequest;
import software.amazon.awssdk.services.ec2.model.Reservation;

import java.util.List;

@Service
public class EC2Service extends AwsSdkClientAware<Ec2Client> {

    @Autowired
    public EC2Service(CliEnvironment cliEnvironment) {
        super(cliEnvironment);
    }

    public void list() {
        List<Reservation> reservations = getClient().describeInstances().reservations();
        PrintUtils.printClassicTable(new EC2TableModelTranslator(reservations).translate());
    }

    public void describe(String instanceId) {
        DescribeInstancesRequest req = DescribeInstancesRequest.builder().instanceIds(instanceId).build();
        List<Reservation> reservations = getClient().describeInstances(req).reservations();
        PrintUtils.printClassicTable(new EC2DetailsTableModelTranslator(reservations).translate());
    }

    @Override
    Ec2Client buildClient(CliEnvironment env) {
        return Ec2Client.builder()
                .credentialsProvider(ProfileCredentialsProvider.create(env.getAwsProfile()))
                .build();
    }

}

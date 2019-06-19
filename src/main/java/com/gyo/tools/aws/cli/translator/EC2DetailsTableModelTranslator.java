package com.gyo.tools.aws.cli.translator;

import software.amazon.awssdk.services.ec2.model.GroupIdentifier;
import software.amazon.awssdk.services.ec2.model.Instance;
import software.amazon.awssdk.services.ec2.model.InstanceNetworkInterface;
import software.amazon.awssdk.services.ec2.model.Reservation;

import java.util.List;
import java.util.stream.Collectors;

public class EC2DetailsTableModelTranslator extends TableModelTranslator<Reservation> {

    private static final String[] COL_NAMES = {"ID", "Security Groups", "Private DNS", "Private IP", "Public DNS", "Public IP", "Subnet ID", "IAM", "Network Interfaces"};

    public EC2DetailsTableModelTranslator(List<Reservation> values) {
        super(values);
    }

    @Override
    String[] translateRow(Reservation reservation) {
        String[] row = new String[COL_NAMES.length];
        for (Instance i: reservation.instances()) {
            row[0] = i.instanceId();
            row[1] = i.securityGroups().stream().map(GroupIdentifier::groupName).collect(Collectors.joining(","));
            row[2] = i.privateDnsName();
            row[3] = i.privateIpAddress();
            row[4] = i.publicDnsName();
            row[5] = i.publicIpAddress();
            row[6] = i.subnetId();
            row[7] = i.iamInstanceProfile() == null ? "" : i.iamInstanceProfile().id();
            row[8] = i.networkInterfaces().stream().map(InstanceNetworkInterface::networkInterfaceId).collect(Collectors.joining(","));
        }
        return row;
    }

    @Override
    String[] getColumnNames() {
        return COL_NAMES;
    }
}

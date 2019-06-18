package com.gyo.tools.aws.cli.translator;

import software.amazon.awssdk.services.ec2.model.Instance;
import software.amazon.awssdk.services.ec2.model.Reservation;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EC2TableModelTranslator extends TableModelTranslator<Reservation> {
    private static final String[] COL_NAMES =
            {"Instance ID", "Key Name", "AMI", "Type", "Monitoring", "State", "Created At"};

    public EC2TableModelTranslator(List<Reservation> values) {
        super(values);
    }

    @Override
    String[] translateRow(Reservation reservation) {
        String[] row = new String[COL_NAMES.length];
        for (Instance i: reservation.instances()) {
            row[0] = i.instanceId();
            row[1] = i.keyName();
            row[2] = i.imageId();
            row[3] = i.instanceTypeAsString();
            row[4] = i.monitoring().stateAsString();
            row[5] = i.state().nameAsString();
            row[6] = LocalDateTime.ofInstant(i.launchTime(), ZoneOffset.systemDefault())
                    .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
        return row;
    }

    @Override
    String[] getColumnNames() {
        return COL_NAMES;
    }
}

package com.gyo.tools.aws.cli.translator;

import software.amazon.awssdk.services.iam.model.User;

import java.util.List;

public class IamUserTableModelTranslator extends TableModelTranslator<User>{
    private static final String[] COL_NAMES = {"ID", "User", "Created At", "Password Last Used"};

    public IamUserTableModelTranslator(List<User> values) {
        super(values);
    }

    @Override
    String[] translateRow(User value) {
        return List.of(
                    value.userId(),
                    value.userName(),
                    formatLocalDateFromInstant(value.createDate()),
                    formatLocalDateTimeFromInstant(value.passwordLastUsed())
                )
                .toArray(new String[]{});
    }

    @Override
    String[] getColumnNames() {
        return COL_NAMES;
    }
}

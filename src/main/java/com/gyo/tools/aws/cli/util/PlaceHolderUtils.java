package com.gyo.tools.aws.cli.util;

import com.gyo.tools.aws.cli.model.CliProfileHolder;
import org.springframework.util.PropertyPlaceholderHelper;

import java.util.Properties;

public final class PlaceHolderUtils {

    public static String formatStringWithEnvVariables(final String value) {
        Properties envProps = new Properties();
        envProps.putAll(CliProfileHolder.instance().getEnvMap());
        return new PropertyPlaceholderHelper("${", "}").replacePlaceholders(value, envProps);
    }
}

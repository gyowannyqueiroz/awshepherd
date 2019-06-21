package com.gyo.tools.aws.cli.config;

import com.gyo.tools.aws.cli.model.CliEnvironment;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;

@Component
public class CustomPromptProvider implements PromptProvider {

    @Autowired
    private CliEnvironment env;

    @Override
    public AttributedString getPrompt() {
            return new AttributedString(env.getAwsProfile() + ":>",
                    AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW));
    }

}

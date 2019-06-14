package com.gyo.tools.aws.cli.config;

import com.gyo.tools.aws.cli.model.CliProfileHolder;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;

@Component
public class CustomPromptProvider implements PromptProvider {

    @Override
    public AttributedString getPrompt() {
            return new AttributedString(CliProfileHolder.getAwsProfile() + ":>",
                    AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW));
    }

}

package com.gyo.tools.aws.cli.shell;

import com.gyo.tools.aws.cli.event.ProfileChangedEventPublisher;
import com.gyo.tools.aws.cli.model.CliEnvironment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class CliShellTest extends ShellTestSupport {

    @MockBean
    private ProfileChangedEventPublisher profileChangedEventPublisher;

    @MockBean
    private CliEnvironment cliEnvironment;

    private CliShell underTest;

    @Before
    public void setUp() throws Exception {
        underTest = new CliShell(profileChangedEventPublisher, cliEnvironment);
    }

    @Test
    public void should_set_aws_profile() {
        // Given
        String expectedProfile = "default";
        when(cliEnvironment.profileExists(any())).thenReturn(Boolean.TRUE);

        // When
        underTest.setAwsProfile(expectedProfile);

        // Then
        verify(cliEnvironment).setAwsProfile(eq(expectedProfile));
        verify(profileChangedEventPublisher).publishEvent(eq(expectedProfile));
    }

    @Test
    public void should_print_error_message_for_inexistent_profile() {
        // Given
        when(cliEnvironment.profileExists(any())).thenReturn(Boolean.FALSE);
        String profile = "inexistent";

        // When
        underTest.setAwsProfile(profile);

       // Then
       verify(cliEnvironment, never()).setAwsProfile(any());
       String errorMessage = "Profile " + profile +" is not defined in the credentials file." +
               "\nUse 'profiles' command to list the available profiles.";
       assertThat(outContent.toString()).contains(errorMessage);
    }

    @Test
    public void should_list_aws_profiles_loaded_from_credentials_file_by_aws_sdk() throws IOException {
        // Given
        List<String> profiles = List.of("default","tools");
        when(cliEnvironment.getAwsProfiles()).thenReturn(profiles);

        // When
        underTest.listAwsProfiles();

        // Then
        assertThat(outContent.toString()).contains(String.join("\n", profiles));
    }

    @Test
    public void should_refresh_aws_profiles_list() {
        // When
        underTest.refreshAwsProfiles();

        // Then
        verify(cliEnvironment).loadAwsProfiles();
    }

    @Test
    public void should_set_an_environment_variable() {
        // Given
        String name = "env_var";
        String value = "env_val";

        // When
        underTest.setEnvironmentVariable(name, value);

        // Then
        verify(cliEnvironment).putEnv(eq(name), eq(value));
    }

    @Test
    public void should_list_all_environment_variables() {
        // Given
        Map<String,String> expectedEnvs = Map.of("env", "value");
        when(cliEnvironment.getEnvMap()).thenReturn(expectedEnvs);

        // When
        underTest.listEnvironmentVariables();

        // Then
        verify(cliEnvironment).getEnvMap();
        assertThat(outContent.toString()).contains("env = value");
    }
}
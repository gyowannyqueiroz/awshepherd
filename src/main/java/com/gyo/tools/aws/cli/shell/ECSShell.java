package com.gyo.tools.aws.cli.shell;

import com.gyo.tools.aws.cli.model.CliEnvironment;
import com.gyo.tools.aws.cli.util.PrintUtils;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.services.ecs.EcsClient;
import software.amazon.awssdk.services.ecs.model.*;

import java.util.List;

@ShellComponent
public class ECSShell extends AbstractShell {

    private EcsClient ecsClient;

    public ECSShell(CliEnvironment cliEnvironment) {
        super(cliEnvironment);
        buildClient();
    }

    @ShellMethod(key = "ecs-ls", value = "List ecs tasks")
    public void listEcs() {
        ecsClient.listClusters().clusterArns()
                .forEach(clusterArn -> {
                    PrintUtils.printSuccess("\n");
                    DescribeClustersRequest dcReq = DescribeClustersRequest.builder().clusters(clusterArn).build();
                    ecsClient.describeClusters(dcReq).clusters()
                            .forEach(dc -> PrintUtils.printSuccess(dc.clusterName() + " - Status: " + dc.status()));
                    String clusterSeparator = "-".repeat(clusterArn.length());
                    PrintUtils.printSuccess(clusterSeparator);

                    ListServicesRequest req = ListServicesRequest.builder().cluster(clusterArn).build();
                    ecsClient.listServices(req).serviceArns()
                            .forEach(serviceArn -> {
                                PrintUtils.printSuccess("\t" + serviceArn);
                            });

                    ListContainerInstancesRequest contReq = ListContainerInstancesRequest.builder().cluster(clusterArn).build();
                    PrintUtils.printSuccess("\t--");
                    ecsClient.listContainerInstances(contReq).containerInstanceArns()
                            .forEach(contInstArn -> {
                                PrintUtils.printSuccess("\t" + contInstArn);
                                ListTasksRequest ltReq = ListTasksRequest.builder().cluster(clusterArn).containerInstance(contInstArn).build();
                                List<String> tasksArns = ecsClient.listTasks(ltReq).taskArns();
                                if (!tasksArns.isEmpty()) {
                                    DescribeTasksRequest dtReq = DescribeTasksRequest.builder().cluster(clusterArn).tasks(tasksArns).build();
                                    ecsClient.describeTasks(dtReq).tasks()
                                            .forEach(t -> {
                                                PrintUtils.printSuccess("\t\t"+t.launchTypeAsString()+" TASK: " + t.startedAt() + " - " + t.lastStatus());
                                                t.containers().forEach(cont -> PrintUtils.printSuccess("\t\t\tCONTAINER: " + cont.name() + " - " + cont.lastStatus() + " - " + cont.reason()));
                                            });
                                    PrintUtils.printSuccess("\n");
                                }
                            });



                });

    }

    private void buildClient() {
        ecsClient = EcsClient.builder()
                .credentialsProvider(ProfileCredentialsProvider.create(getCliEnvironment().getAwsProfile()))
                .build();
    }

    @Override
    void resetServices() {
        buildClient();
    }
}

package com.gyo.tools.aws.cli.service;

import com.gyo.tools.aws.cli.model.S3Bucket;
import com.gyo.tools.aws.cli.util.PrintUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import org.yaml.snakeyaml.representer.Representer;

@Service
public class CFService {

    private static final String CF_HEAD = "AWSTemplateFormatVersion: 2010-09-09\n" +
            "Resources:";

    @Autowired
    private S3Service s3Service;

    public void extractForS3Bucket(String bucket) {
        PropertyUtils propUtils = new PropertyUtils();
        propUtils.setAllowReadOnlyProperties(true);

        Representer repr = new Representer();
        repr.setPropertyUtils(propUtils);

        DumperOptions dumper = new DumperOptions();
        dumper.setDefaultFlowStyle(DumperOptions.FlowStyle.AUTO);
        dumper.setPrettyFlow(true);

        Yaml yaml = new Yaml(dumper);
        String content = yaml.dump(s3Service.describe(bucket));
        PrintUtils.printSuccess(convertToCloudFormationYaml(S3Bucket.class, content));
    }

    private String convertToCloudFormationYaml(Class<?> clazz, String content) {
        String yaml = content.replaceAll("!!"+clazz.getCanonicalName(), "").replaceAll("\\{", "").replaceAll("}", "");
        yaml = CF_HEAD + yaml;
        return yaml;
    }

}

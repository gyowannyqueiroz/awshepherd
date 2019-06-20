```bash
  ___   _    _  _____  _                   _                      _
 / _ \ | |  | |/  ___|| |                 | |                    | |
/ /_\ \| |  | |\ `--. | |__    ___  _ __  | |__    ___  _ __   __| |
|  _  || |/\| | `--. \| '_ \  / _ \| '_ \ | '_ \  / _ \| '__| / _` |
| | | |\  /\  //\__/ /| | | ||  __/| |_) || | | ||  __/| |   | (_| |
\_| |_/ \/  \/ \____/ |_| |_| \___|| .__/ |_| |_| \___||_|    \__,_|
                                   | |
                                   |_|
```
[![Build Status](https://travis-ci.com/gyowannyqueiroz/awshepherd.svg?branch=master)](https://travis-ci.com/gyowannyqueiroz/awshepherd)

### What is it?
This is just another client made with Java and Spring Shell framework on top of the AWS Java SDK that aims to automate trivial
AWS tasks and to save some time on typing all the commands and flags available in the oficial AWS CLI.

### Built-in Functionalities

- Ability to list the available profiles from your AWS credentials file. Actually it just uses AWS SDK existing functionality.
- Profile switch. You set the profile and execute as many commands you need. The current profile is displayed as part of the prompt.
- Environment variables. You can set variables and then use place holders instead of ctrl+c & ctrl+v. e.g: 


```
default:>env S3_DOWNLOAD_FOLDER=/home/theresa/downloads
default:>env S3_SOURCE_BUCKET=my-bucket
``` 


then use as place holder for your command:

```s3-pull ${S3_SOURCE_BUCKET}/myfile.txt ${S3_DOWNLOAD_FOLDER}``` 


- Run the ```help``` command to get a list of the available commands and ```help <command>``` for command details



#!/bin/bash

PRONAME=PersonalAssistant-1.0-SNAPSHOT.jar
nohup java -jar ${PRONAME} &

cd install
chmod +x *.sh 
./crontab_install.sh 

echo "[Program stat:========]"
ps -ef | grep ${PRONAME}| grep -v grep


#需要java命令放在/bin/（其它没有尝试）目录下
#需要安装crontab
#yum install vixie-cron crontabs
#手动启动crontab服务：service crond start, 查看crontab服务状态：service crond status
#将crontab设置为开机启动
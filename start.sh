#!/bin/bash

PRONAME=PersonalAssistant-1.0-SNAPSHOT.jar

num=`ps -ef|grep ${PRONAME}|grep -v grep|grep -v monitor|wc -l`
if [ ${num} -ge 1 ]
then
    echo ==============================================================================================
    echo "Warning : Program [${PRONAME}] is running ,please kill the running program first before start!"
    echo ==============================================================================================
    exit 0
fi

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
#将crontab设置为开机启动chkconfig --level 35 crond on
#!/bin/bash

PRONAME=PersonalAssistant-1.0-SNAPSHOT.jar

pid=`ps -ef|grep ${PRONAME}|grep -v grep|awk '{print $2}'`
if [ ${pid} ]
then
        echo "kill ${PRONAME} first"
        kill -9 ${pid}
        echo "killed done"
fi
echo "`date` restart $0 " >> restart.log
eval "./start.sh"
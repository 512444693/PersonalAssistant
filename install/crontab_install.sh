#!/bin/bash
CRONTAB_FILE="crontab_PersonalAssistant_file"
chmod 644 $CRONTAB_FILE
chown root $CRONTAB_FILE
chgrp root $CRONTAB_FILE
cp $CRONTAB_FILE /etc/cron.d/
touch /etc/cron.d/* 

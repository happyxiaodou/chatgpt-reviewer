#!/bin/sh

PROJECT=$(pwd)
RES_DIR=${PROJECT}/lib
JAR_NAME=chatgpt-codereview.jar
JAVA_OPTS="-Xms256M -Xmx256M -XX:+UseConcMarkSweepGC -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=65  -XX:+HeapDumpOnOutOfMemoryError -Dfile.encoding=utf-8"

APP_MAINCLASS=chatgpt-codereview

checkpid() {
  javaps=$(jps -l | grep $APP_MAINCLASS)

  if [ -n "$javaps" ]; then
    psid=$(echo $javaps | awk '{print $1}')
  else
    psid=0
  fi
}

start() {
  checkpid

  if [ $psid -ne 0 ]; then
    echo "================================"
    echo "warn: $APP_MAINCLASS already started! (pid=$psid)"
    echo "================================"
  else
    echo -n "Starting $APP_MAINCLASS ..."
    $(nohup java $JAVA_OPTS -Dloader.path=$RES_DIR -jar ${PROJECT}/${JAR_NAME} --spring.profiles.active=prod >/dev/null 2>&1 &)
    checkpid
    if [ $psid -ne 0 ]; then
      echo "(pid=$psid) [OK]"
    else
      echo "[Failed]"
    fi
  fi
}

start

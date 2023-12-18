#!/bin/sh


APP_MAINCLASS=chatgpt-codereview

jpsid() {
   javapid=`jps -l | grep $APP_MAINCLASS | awk '{print $1}'`
   echo "javapid: $javapid"
   return javapid
}

checkpid() {
   javapid=`jps -l | grep $APP_MAINCLASS | awk '{print $1}'`
   echo "javapid: $javapid"
   if [ -n "$javapid" ]; then
      javaps=`ps -ef | grep "$javapid" | grep java | awk '{print $2}'`
      echo "jps $javaps"
      if [ -n "$javaps" ]; then
         psid=$javaps
      else
         psid=0
      fi
   else
      psid=0
   fi
}

stop() {
   checkpid
   if [ $psid -ne 0 ]; then
      echo -n "Stopping $APP_MAINCLASS ...(pid=$psid) "
      `kill -15 $psid`
      if [ $? -eq 0 ]; then
         echo "[OK]"
         return
      else
         echo "[Failed]"
      fi

      # checkpid
      # if [ $psid -ne 0 ]; then
      #    stop
      # fi
   else
      echo "================================"
      echo "warn: $APP_MAINCLASS is not running"
      echo "================================"
   fi
}

stop;

#!/bin/bash
set -x
JAVA="$JAVA_HOME/bin/java"
OPT_FIX="--add-opens java.base/java.lang=ALL-UNNAMED"
BASE_DIR=$(cd "$(dirname "$0")"/.. || exit; pwd)
ZGC="-XX:+UseZGC -Xlog:gc"
JAVA_OPT="${OPT_FIX} ${ZGC} -jar -Dspring.profiles.active=prod"
SERVICE_PREFIX="changliulab-"
JAR="changliulab-standalone*"

checkPid() {

  echo "Checking if the services are running"
  javaPs=$("$JAVA_HOME"/bin/jps -l | grep "${SERVICE_PREFIX}")

  if [ -n "$javaPs" ]; then

    echo "Following PID are the Services"
   pSid=$(echo "$javaPs" | awk '{print $1}')

  else

  echo "No service is running"
   pSid=0

  fi
  echo ${pSid}

}
killCurrentRunning(){
  if [ -n "${pSid}"  ]; then
    echo "========================"
    echo "Start kill follow process"
    echo ${pSid}
    kill -9 ${pSid}
  fi
}
run(){
#  echo "nohup ${JAVA} ${JAVA_OPT} ${BASE_DIR}/jar/${JAR}  >> ${BASE_DIR}/logs/${JAR}.log 2>&1 &"
  nohup "${JAVA}" ${JAVA_OPT} ${JAR} 2>&1 &
#  sleep $START_WAIT_TIMEOUT
}
#nohup "${JAVA}" ${JAVA_OPT} "${BASE_DIR}/lab_jar/${JAR}" 2>&1 &

startService(){
  echo "=========="
  checkPid
  if [  "${pSid}" != 0 ]; then
    echo "Services are running "
    killCurrentRunning
    startService
  else
    echo "============="
#    JAR_NAME=$(find lab_jar -name "${GATEWAY}")

    JAR=$(find "${BASE_DIR}/bin" -name "${JAR}")
#   JAVA_OPT="-Xms128M-Xmx128M -jar"
    run


  fi
}

startService

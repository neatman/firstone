#!/bin/sh

BLOG_SERVICE_HOME=/opt/www/mtc-push/trunk/mtc-push

LIB_DIR=${BLOG_SERVICE_HOME}/lib

LOGS_DIR=${BLOG_SERVICE_HOME}/logs

ARCHIVE_SUFFIX=`date +%Y%m%d-%H%M`

MAIN_CLASS="com.sohu.mtc.push.PushServer"

JAVA_ARGS="-server -Xms512m -Xmx512m -XX:NewSize=100m -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=58 -XX:PermSize=64m -XX:MaxPermSize=64m -XX:ThreadStackSize=512 -Xloggc:${BLOG_SERVICE_HOME}/logs/gc.log"

JAVA_ARGS="${JAVA_ARGS}  -Dsun.rmi.transport.tcp.readTimeout=5000  -Dsun.rmi.dgc.server.gcInterval=3600000 -Dsun.rmi.dgc.client.gcInterval=3600000 -Dsun.rmi.server.exceptionTrace=true -Drmi.zk_env=client_online -Dginkgo.client.host=192.168.106.129 -Dginkgo.client.whoami=client -DlogDir=/home"


CLASSPATH=$CLASSPATH:${BLOG_SERVICE_HOME}/target/classes/
files=`ls -1 ${LIB_DIR}`
for file in ${files} ;do
	CLASSPATH=$CLASSPATH:${LIB_DIR}/${file}
done

#if [  -f ${LOGS_DIR}/stdout.log ]; then 
#	mv ${LOGS_DIR}/stdout.log ${LOGS_DIR}/stdout.log.${ARCHIVE_SUFFIX} 
#fi

if [  -f ${LOGS_DIR}/stderr.log ]; then 
	mv ${LOGS_DIR}/stderr.log ${LOGS_DIR}/stderr.log.${ARCHIVE_SUFFIX} 
fi

if [  -f ${LOGS_DIR}/gc.log ]; then 
	mv ${LOGS_DIR}/gc.log ${LOGS_DIR}/gc.log.${ARCHIVE_SUFFIX} 
fi

java ${JAVA_ARGS} ${MAIN_CLASS} 1>/dev/null  2>${BLOG_SERVICE_HOME}/logs/stderr.log&
echo "service is starting..."
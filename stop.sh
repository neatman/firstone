#!/bin/sh

pushid=`ps auxf | grep "/opt/www/mtc-push/trunk/mtc-push" | grep -v grep | grep -v "/bin/sh" | awk -F ' ' '{print $2}'`
echo "push server pid is ${pushid}";
if [ -n $pushid ] 
then
	  kill $pushid
	  echo "$pushid is killed!"
fi
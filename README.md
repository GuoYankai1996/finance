# Binance Java SDK (beta version)

This is Binance Java SDK, This is a lightweight Java library, you can import to your Java project and use this SDK to query all market data, trading and manage your account.

The SDK supports both synchronous and asynchronous RESTful API invoking, and subscribe the market data from the Websocket connection.


## Table of Contents

- [Beginning](#Beginning)
  - [Installation](#Installation)


## Beginning

### Installation

*The SDK is compiled by Java8*

For Beta version, please import the source code in java IDE (idea or eclipse)

The example code is in binance-api-sdk/java/src/test/java/com/binance/client/examples.

## 启动脚本
#!/bin/bash
#静态资源上传路径
WEB_ROOT=/usr/el/tomcat-xzel/webapps/qh-sso
#jar地址
SERVCER_JAR=binance-client-1.0.0.jar
#日志名称
LOG_NAME=info.log
#进入对应目录
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
#输出信息
echo '---------------------------------------'
echo $DIR
#创建项目jar包工程目录
if [ ! -d /app/jar ];then
  mkdir -p /app/jar
fi
#创建备份目录
date_stamp=`date "+%Y-%m-%d-%H-%M-%S"`
echo $date_stamp
if [ ! -d /app/bak/jar$date_stamp ];then
  mkdir -p /app/bak/jar$date_stamp
fi
#备份
if [ ! -f "/app/jar/$SERVCER_JAR" ];then
echo $SERVCER_JAR"文件不存在"
else
cp /app/jar/$SERVCER_JAR  /app/bak/jar$date_stamp
fi
#杀死java进程
_pid=`ps -ef | grep java | grep $SERVCER_JAR |awk '{print $2}'`
echo "kill ------>"$SERVCER_JAR "pid---->" $_pid
[ -n "$_pid" ] && kill -9 $_pid
#删除老包，替换新包
rm -rf /app/jar/$SERVCER_JAR
cp $DIR/$SERVCER_JAR  /app/jar
#启动新包
if [ ! -f "/app/logs/$LOG_NAME" ];then
echo "文件不存在，创建新文件"
mkdir -p /app/logs/
touch /app/logs/$LOG_NAME
fi
JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.161-2.b14.el7.x86_64
JAVA=$JAVA_HOME/bin/java
echo '---------------------------------------java path'
echo $JAVA
nohup java -Dweb_root=${WEB_ROOT} -Xmx300m -Xms300m  -XX:PermSize=500M -XX:MaxPermSize=500M -Xss256k -jar  /app/jar/$SERVCER_JAR  > /app/logs/$LOG_NAME 2>&1 &
#三十秒后打印日志
sleep 30
cat /app/logs/$LOG_NAME


#!/bin/bash

THIS_DIR=$(dirname "$0")
cd "$THIS_DIR"

echo "Host: $(hostname)"

NUM_SAMPLES=20
NUM_MESSAGES=10000
SUFFIX=$RANDOM
echo $SUFFIX

TEST_RESULT=${TMPDIR:-/tmp}/test_result-$SUFFIX.txt
SERVER_RESULT=${TMPDIR:-/tmp}/server_result-$SUFFIX.txt

echo "Test result in $TEST_RESULT"
echo "Server result in $SERVER_RESULT"

if [ -d "/c/Program Files/Java/jdk1.7.0_60/bin" ]; then
    PATH="$PATH:/c/Program Files/Java/jdk1.7.0_60/bin"
fi

if [ -d "/c/Program Files (x86)/ZeroC/Ice-3.5.1/bin/x64" ]; then
    PATH="$PATH:/c/Program Files (x86)/ZeroC/Ice-3.5.1/bin/x64"
fi

if [ -d "$HOME/local-install/lib" ]; then
    LD_LIBRARY_PATH="$LD_LIBRARY_PATH:$HOME/local-install/lib"
fi

finish() {
    rm -f "$TEST_RESULT" "$SERVER_RESULT"
}
#trap finish EXIT

runBenchmark() {
  local i
  echo "Testing: $@"
  echo > "$TEST_RESULT"
  for ((i = 0; i < $NUM_SAMPLES; i++)) {
    echo -n "."
    sleep 1
    eval "$@" >> "$TEST_RESULT"
  }
  awk 'BEGIN { n=0; } /Average latency/ {ms = $4; val[n] = $5; n+=1; sum+=$5; } END { mean=sum/n; sd = 0; for (i=0;i<n;i++) { sd += (val[i] - mean)^2; }; sd = sqrt(sd/(n-1)); print "Computed",n,"samples"; print "Average latency in", ms,mean; print "Sample standard deviation in", ms,sd; if (ms ~ /milliseconds/) { print "Average latency in microseconds:", mean*1000.0 ; print "Sample standard deviation in microseconds:", sd*1000.0 ; } }' "$TEST_RESULT"
  echo
}

# $1 - server cmd
# $2 - client cmd
runServerClientBenchmark() {
    local server=$1
    local client=$2

    echo "Starting $server..."
    rm -f "$SERVER_RESULT"
    local ppid
    eval '$server > "$SERVER_RESULT" 2>&1 & ppid=$!'

    while ! grep "Starting" "$SERVER_RESULT" >/dev/null  2>&1; do
        sleep 1
        if ! kill -0 $ppid; then
            echo "Process $server is not running !!!"
            return 1
        fi
    done
    sleep 2

    if ! kill -0 $ppid; then
        echo "Process $server is not running !!!"
        return 1
    fi

    runBenchmark "$client"
    kill $ppid
    sleep 1
}

if [ -n "$1" ]; then
    runBenchmark "$@"
    exit $?
fi

if true; then
	CLASSPATH=$HOME/.m2/repository/org/apache/thrift/libthrift/0.9.1/libthrift-0.9.1.jar:\
$HOME/.m2/repository/org/slf4j/slf4j-log4j12/1.5.6/slf4j-log4j12-1.5.6.jar:\
$HOME/.m2/repository/commons-codec/commons-codec/1.6/commons-codec-1.6.jar:\
$HOME/.m2/repository/commons-logging/commons-logging/1.1.1/commons-logging-1.1.1.jar:\
$HOME/.m2/repository/org/apache/commons/commons-lang3/3.1/commons-lang3-3.1.jar:\
$HOME/.m2/repository/log4j/log4j/1.2.14/log4j-1.2.14.jar:\
$HOME/.m2/repository/org/slf4j/slf4j-api/1.5.8/slf4j-api-1.5.8.jar:\
$HOME/.m2/repository/com/zeroc/ice/3.5.1/ice-3.5.1.jar:.:\
$HOME/.m2/repository/com/rabbitmq/amqp-client/3.3.1/amqp-client-3.3.1.jar:\
$HOME/.m2/repository/commons-lang/commons-lang/2.6/commons-lang-2.6.jar:\
ApacheThriftProject/target/classes/:\
TCPSocketProject/target/classes/:\
RMIJavaProject/target/classes/:\
ZerocIceProject/target/classes/:\
RabbitMQJava/target/classes/
	#runServerClientBenchmark "java -cp $CLASSPATH dfki.sb.tcpsocketproject.TCPServer infinite" "java -cp $CLASSPATH dfki.sb.tcpsocketproject.TCPClient"
	#runServerClientBenchmark "java -cp $CLASSPATH dfki.sb.tcpsocketproject.TCPObjectServer infinite" "java -cp $CLASSPATH dfki.sb.tcpsocketproject.TCPObjectClient"
	#runServerClientBenchmark "java -cp $CLASSPATH dfki.sb.rmijavaproject.RMIJavaServer" "java -cp $CLASSPATH dfki.sb.rmijavaproject.RMIJavaClient"		
	#runServerClientBenchmark "java -cp $CLASSPATH dfki.sb.apachethriftproject.ThriftJavaServer" "java -cp $CLASSPATH dfki.sb.apachethriftproject.ThriftJavaClient"
	#runServerClientBenchmark "java -cp $CLASSPATH dfki.sb.zerociceproject.IceJavaServer" "java -cp $CLASSPATH dfki.sb.zerociceproject.IceJavaClient"
	#runServerClientBenchmark "java -cp $CLASSPATH dfki.sb.rabbitmqjava.RabbitMQServer" "java -cp $CLASSPATH dfki.sb.rabbitmqjava.RabbitMQClient"
	#runServerClientBenchmark "java -cp $CLASSPATH dfki.sb.rabbitmqjava.RabbitMQObjectStreamServer infinite" "java -cp $CLASSPATH dfki.sb.rabbitmqjava.RabbitMQObjectStreamClient"
    runServerClientBenchmark "java -cp $CLASSPATH de.dfki.kiara.test.BenchmarkServer" "java -cp $CLASSPATH de.dfki.kiara.test.BenchmarkClient"
fi

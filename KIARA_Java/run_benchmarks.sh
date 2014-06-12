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
}

if [ -n "$1" ]; then
    runBenchmark "$@"
    exit $?
fi

if true; then
runBenchmark "perl tools/boost_test.pl Boost 1000 $NUM_MESSAGES"
runBenchmark "perl tools/boost_test.pl Boost 472 $NUM_MESSAGES"
runBenchmark "perl tools/boost_test.pl BoostTyped $NUM_MESSAGES"
runBenchmark "perl tools/boost_test.pl BoostTyped2 $NUM_MESSAGES"
fi

runServerClientBenchmark "KiaraTypedSubscriber 8080 ortecdr" KiaraTypedPublisher

runServerClientBenchmark "KiaraTypedSubscriber 8080 dummy" KiaraTypedPublisher

THRIFT_SERVER=$PWD/ThriftTest/ThriftServer
THRIFT_CLIENT=$PWD/ThriftTest/ThriftClient

if ! [ -e "$THRIFT_SERVER" -a -e "$THRIFT_CLIENT" ]; then
    THRIFT_SERVER=$PWD/ThriftTest/x64/Release/ThriftServer.exe
    THRIFT_CLIENT=$PWD/ThriftTest/x64/Release/ThriftClient.exe
fi

if [ -e "$THRIFT_SERVER" -a -e "$THRIFT_CLIENT" ]; then
    runServerClientBenchmark "$THRIFT_SERVER" "$THRIFT_CLIENT $NUM_MESSAGES localhost"
else
    echo "No Thrift server/client found !"
fi

# Ice

ICE_SERVER=$PWD/IceTest/IceServer
ICE_CLIENT=$PWD/IceTest/IceClient

if ! [ -e "$ICE_SERVER" -a -e "$ICE_CLIENT" ]; then
    ICE_SERVER=$PWD/IceTest/x64/Release/IceServer.exe
    ICE_CLIENT=$PWD/IceTest/x64/Release/IceClient.exe
fi

if [ -e "$ICE_SERVER" -a -e "$ICE_CLIENT" ]; then
    runServerClientBenchmark "$ICE_SERVER" "$ICE_CLIENT $NUM_MESSAGES localhost"
else
    echo "No Ice server/client found !"
fi

# echo "Starting KIARA server with ortecdr"
# KiaraTypedSubscriber 8080 ortecdr > server_result.txt &
# ppid=$!

# while ! grep "Starting" server_result.txt >/dev/null  2>&1; do
#   sleep 1
# done
# sleep 2

# echo "Running KIARA clients"

# runBenchmark "KiaraTypedPublisher"
# kill -s SIGTERM $ppid

# Thrift

# THRIFT_SERVER=$PWD/ThriftTest/Server
# THRIFT_CLIENT=$PWD/ThriftTest/Client

# if ! [ -e "$THRIFT_SERVER" -a -e "$THRIFT_CLIENT" ]; then
#     THRIFT_SERVER=$PWD/ThriftTest/x64/Release/ThriftServer.exe
#     THRIFT_CLIENT=$PWD/ThriftTest/x64/Release/ThriftClient.exe
# fi

# if [ -e "$THRIFT_SERVER" -a -e "$THRIFT_CLIENT" ]; then
#     echo "Starting Thrift server"
#     "$THRIFT_SERVER" > server_result.txt 2>&1 &
#     ppid=$!

#     while ! grep "Starting" server_result.txt >/dev/null  2>&1; do
#         sleep 1
#     done
#     sleep 2

#     echo "Running Thrift clients"

#     runBenchmark "$THRIFT_CLIENT 1000 localhost"
#     kill -s SIGTERM $ppid
# fi

# # ICE

# ICE_SERVER=$PWD/IceTest/Server
# ICE_CLIENT=$PWD/IceTest/Client

# if ! [ -e "$ICE_SERVER" -a -e "$ICE_CLIENT" ]; then
#     ICE_SERVER=$PWD/IceTest/x64/Release/IceServer.exe
#     ICE_CLIENT=$PWD/IceTest/x64/Release/IceClient.exe
# fi

# if [ -e "$ICE_SERVER" -a -e "$ICE_CLIENT" ]; then
#     echo "Starting Ice server"
#     "$ICE_SERVER" > server_result.txt 2>&1 &
#     ppid=$!

#     while ! grep "Starting" server_result.txt >/dev/null  2>&1; do
#         sleep 1
#     done
#     sleep 2

#     echo "Running Ice clients"

#     runBenchmark "$ICE_CLIENT 1000 localhost"
#     kill -s SIGTERM $ppid
# fi

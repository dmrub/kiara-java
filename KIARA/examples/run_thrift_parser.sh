#!/bin/bash

CLASSPATH=$CLASSPATH:$PWD/../target/classes
java de.dfki.kiara.tool.ThriftIDLParser  tutorial.thrift

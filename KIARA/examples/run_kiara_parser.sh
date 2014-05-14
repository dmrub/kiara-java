#!/bin/bash

CLASSPATH=$CLASSPATH:$PWD/../target/classes
java de.dfki.kiara.tool.KiaraIDLParser  "$1"

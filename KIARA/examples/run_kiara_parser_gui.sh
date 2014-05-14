#!/bin/bash

CLASSPATH=$CLASSPATH:$PWD/../target/classes
java org.antlr.v4.runtime.misc.TestRig de.dfki.kiara.idl.Kiara program -gui < "$1"

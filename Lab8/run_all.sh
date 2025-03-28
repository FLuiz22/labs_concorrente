#!/bin/bash

args=`find dataset -type f | xargs`

echo "Building Java Serial/Concurrent implementation"
bash src/java/build.sh

echo "Running Serial implementation"
time bash src/java/run.sh $args

echo "Running Concurrent implementation 1"
time bash src/java/run2.sh $args
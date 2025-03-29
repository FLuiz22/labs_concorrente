#!/bin/bash

args=`find dataset -type f | xargs`

echo "Building Java Serial/Concurrent implementation"
bash src/java/build.sh

echo "Running Serial implementation"
time bash src/java/run.sh $args

echo "Running Concurrent implementation 1"
time bash src/java/run2.sh $args

echo "Running Concurrent implementation 2.1"
time bash src/java/run3_1.sh $args

echo "Running Concurrent implementation 2.2"
time bash src/java/run3_2.sh $args

echo "Running Concurrent implementation 2.3"
time bash src/java/run3_3.sh $args

echo "Running Concurrent implementation 3"
time bash src/java/run4.sh $args
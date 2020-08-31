#!/bin/bash


TRACE_DIR=trace-`date +%s`
GIT_REV=`git rev-parse HEAD`
GIT_BRANCH=`git rev-parse --abbrev-ref HEAD`

mkdir -p $TRACE_DIR/$GIT_REV

for numNodes in 500 5000 10000;
do
   startTimeCutOff=200000
   ./gradlew runBenchmark --args="-n ${numNodes} -f v2-cropped.txt -c 100 -m 200 -t 100 -s ${startTimeCutOff} -p 100" &> /tmp/out
   
   expId=`date +%s`
   mkdir -p $TRACE_DIR/$GIT_REV/$expId
   cp /tmp/out $TRACE_DIR/$GIT_REV/$expId/workload_output
   cp /tmp/out $TRACE_DIR/$GIT_REV/$expId/dcm_scheduler_trace

   echo "workload,schedulerName,solver,kubeconfig,dcmGitBranch,dcmGitCommitId,numNodes,startTimeCutOff,percentageOfNodesToScoreValue,timeScaleDown,affinityProportion" > $TRACE_DIR/$GIT_REV/$expId/metadata
   echo "v2-cropped.txt,dcm-scheduler,ORTOOLS,local,$GIT_BRANCH,$GIT_REV,$numNodes,$startTimeCutOff,0,100,100" >> $TRACE_DIR/$GIT_REV/$expId/metadata
done
